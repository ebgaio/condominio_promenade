package br.com.edificiopromenade.presentation.despesa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.edificiopromenade.domain.model.CadastrarDespesaCommand
import br.com.edificiopromenade.domain.model.ExcluirDespesaCommand
import br.com.edificiopromenade.domain.repository.FechamentoRepository
import br.com.edificiopromenade.domain.usecase.despesa.CadastrarDespesaUseCase
import br.com.edificiopromenade.domain.usecase.despesa.ConsultarDespesasPorFechamentoUseCase
import br.com.edificiopromenade.domain.usecase.despesa.ExcluirDespesaUseCase
import br.com.edificiopromenade.domain.usecase.despesa.VerificarDespesaExistenteUseCase
import br.com.edificiopromenade.domain.usecase.fechamento.FinalizarFechamentoUseCase
import br.com.edificiopromenade.presentation.common.message.UiMessage
import br.com.edificiopromenade.presentation.common.mapper.toUi
import br.com.edificiopromenade.presentation.common.model.DespesaUi
import br.com.edificiopromenade.presentation.common.model.DespesaItemUi
import br.com.edificiopromenade.presentation.tipodespesa.usecase.ConsultarTiposDespesaUiUseCase
import br.com.edificiopromenade.presentation.util.MoneyFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DespesaViewModel @Inject constructor(
    private val cadastrarDespesaUseCase: CadastrarDespesaUseCase,
    private val consultarDespesasPorFechamentoUseCase: ConsultarDespesasPorFechamentoUseCase,
    private val excluirDespesaUseCase: ExcluirDespesaUseCase,
    private val consultarTiposDespesaUiUseCase: ConsultarTiposDespesaUiUseCase,
    private val finalizarFechamentoUseCase: FinalizarFechamentoUseCase,
    private val verificarDespesaExistenteUseCase: VerificarDespesaExistenteUseCase,
    private val fechamentoRepository: FechamentoRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            DespesaUiState()
        )

    val uiState = _uiState.asStateFlow()

    private var fechamentoId: Long = 0

    fun carregar(
        fechamentoId: Long
    ) {
        this.fechamentoId = fechamentoId

        viewModelScope.launch {
            val fechamento = fechamentoRepository.findById(
                    fechamentoId
                )

            _uiState.value =
                _uiState.value.copy(
                    fechamentoFinalizado =
                        fechamento?.finalizado ?: false
                )
        }

        viewModelScope.launch {
            consultarDespesasPorFechamentoUseCase(
                fechamentoId
            ).collect { lista ->

                _uiState.value =
                    _uiState.value.copy(
                        despesas = lista.map {
                            it.toUi()
                        }
                    )
            }
        }

        viewModelScope.launch {
            consultarTiposDespesaUiUseCase()
                .collect { listaUi ->

                    _uiState.value =
                        _uiState.value.copy(
                            tiposDespesa = listaUi,
                            tipoSelecionado = _uiState.value.tipoSelecionado
                                    ?: listaUi.firstOrNull()
                        )
                }
        }
    }

    fun onValorChanged(
        valor: String
    ) {
        val cleanValor = valor.filter { it.isDigit() }
        if (cleanValor.length > 12) return

        _uiState.value =
            _uiState.value.copy(
                valor = MoneyFormatter.format(
                    cleanValor
                )
            )
    }

    fun onDescricaoChanged(
        descricao: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                descricaoLivre = descricao
            )
    }

    fun salvar() {
        viewModelScope.launch {

            val valorDespesa = stateToDouble()

            if (valorDespesa == null || valorDespesa <= 0.0) {
                _uiState.value =
                    _uiState.value.copy(
                        mensagem = UiMessage.Error(
                            "Informe um valor válido para a despesa."
                        )
                    )
                return@launch
            }

            val descricao = _uiState.value.descricaoLivre.trim()

            if (
                verificarDespesaExistenteUseCase(
                    fechamentoId,
                    _uiState.value.tipoSelecionado!!.id

                )
            ) {
                _uiState.value =
                    _uiState.value.copy(
                        mensagem = UiMessage.Error(
                        "A despesa '$descricao' já foi cadastrada."
                        )
                    )
                return@launch
            }

            cadastrarDespesaUseCase(
                CadastrarDespesaCommand(
                    fechamentoId = fechamentoId,
                    tipoDespesaId = _uiState.value.tipoSelecionado!!.id,
                    descricaoLivre = _uiState.value.descricaoLivre,
                    valor = valorDespesa
                )
            )

            _uiState.value =
                _uiState.value.copy(
                    valor = "",
                    descricaoLivre = ""
                )

            _uiState.value =
                _uiState.value.copy(
                    valor = "",
                    mensagem = UiMessage.Success(
                     "Despesa cadastrada com sucesso."
                    )
                )
            return@launch
        }
    }

    private fun stateToDouble(): Double? {
        return _uiState.value.valor
            .replace(".", "")
            .replace(",", ".")
            .toDoubleOrNull()
    }

    fun solicitarExclusao(
        despesa: DespesaUi
    ) {
        _uiState.value =
            _uiState.value.copy(
                despesaSelecionadaParaExclusao = despesa
            )
    }

    fun cancelarExclusao() {
        _uiState.value =
            _uiState.value.copy(
                despesaSelecionadaParaExclusao = null
            )
    }

    fun confirmarExclusao() {

        val despesa = _uiState.value
                .despesaSelecionadaParaExclusao
                ?: return

        viewModelScope.launch {

            excluirDespesaUseCase(
                ExcluirDespesaCommand(
                    despesa.id
                )
            )

            _uiState.value =
                _uiState.value.copy(
                    despesaSelecionadaParaExclusao = null,
                    mensagem = UiMessage.Success(
                     "Despesa excluída com sucesso."
                    )
                )
        }
    }

    fun limparMensagem() {
        _uiState.value =
            _uiState.value.copy(
                mensagem = null,
                valor = "",
                descricaoLivre = ""
            )
    }

    fun alterarExpandido(
        expandido: Boolean
    ) {
        _uiState.value =
            _uiState.value.copy(
                expandirTipos = expandido
            )
    }

    fun selecionarTipo(
        tipo: DespesaItemUi
    ) {
        _uiState.value =
            _uiState.value.copy(
                tipoSelecionado = tipo,
                expandirTipos = false
            )
    }

    fun finalizarFechamento(
        onSucesso: () -> Unit
    ) {
        viewModelScope.launch {

            if (_uiState.value.despesas.isEmpty()) {
                _uiState.value =
                    _uiState.value.copy(
                        mensagem = UiMessage.Error(
                            "Cadastre pelo menos uma despesa antes de finalizar o fechamento."
                        )
                    )
                return@launch
            }

            finalizarFechamentoUseCase(
                fechamentoId
            )
            onSucesso()
        }
    }
}