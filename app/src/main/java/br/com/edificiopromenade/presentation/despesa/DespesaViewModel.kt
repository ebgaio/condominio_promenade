package br.com.edificiopromenade.presentation.despesa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.edificiopromenade.data.local.entity.DespesaEntity
import br.com.edificiopromenade.data.local.entity.TipoDespesaEntity
import br.com.edificiopromenade.domain.usecase.despesa.CadastrarDespesaUseCase
import br.com.edificiopromenade.domain.usecase.despesa.ConsultarDespesasPorFechamentoUseCase
import br.com.edificiopromenade.domain.usecase.despesa.ExcluirDespesaUseCase
import br.com.edificiopromenade.domain.usecase.despesa.VerificarDespesaExistenteUseCase
import br.com.edificiopromenade.domain.usecase.fechamento.FinalizarFechamentoUseCase
import br.com.edificiopromenade.domain.usecase.tipodespesa.ConsultarTiposDespesaUseCase
import br.com.edificiopromenade.presentation.common.message.UiMessage
import br.com.edificiopromenade.presentation.util.MoneyFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import jakarta.inject.Inject

@HiltViewModel
class DespesaViewModel @Inject constructor(
    private val cadastrarDespesaUseCase: CadastrarDespesaUseCase,
    private val consultarDespesasPorFechamentoUseCase: ConsultarDespesasPorFechamentoUseCase,
    private val excluirDespesaUseCase: ExcluirDespesaUseCase,
    private val consultarTiposDespesaUseCase: ConsultarTiposDespesaUseCase,
    private val finalizarFechamentoUseCase: FinalizarFechamentoUseCase,
    private val verificarDespesaExistenteUseCase: VerificarDespesaExistenteUseCase
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
            consultarDespesasPorFechamentoUseCase(
                fechamentoId
            ).collect { lista ->

                _uiState.value =
                    _uiState.value.copy(
                        despesas = lista
                    )
            }
        }

        viewModelScope.launch {
            consultarTiposDespesaUseCase()
                .collect { tipos ->

                    _uiState.value =
                        _uiState.value.copy(
                            tiposDespesa = tipos,

                            tipoSelecionado = _uiState.value.tipoSelecionado
                                    ?: tipos.firstOrNull()
                        )
                }
        }
    }

    fun onValorChanged(
        valor: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                valor = MoneyFormatter.format(
                    valor
                )
            )
    }

    fun salvar() {
        viewModelScope.launch {
            val valorDespesa = _uiState.value.valor
                .replace(".", "")
                .replace(",", ".")
                .toDoubleOrNull()
                ?: return@launch

            val descricao =
                _uiState.value.tipoSelecionado
                    ?.descricao
                    ?: return@launch

            if (
                verificarDespesaExistenteUseCase(
                    fechamentoId,
                    descricao
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
                DespesaEntity(
                    fechamentoId = fechamentoId,
                    descricao = _uiState.value.tipoSelecionado
                        ?.descricao
                        ?: "",
                    valor = valorDespesa
                )
            )

            println("SUCESSO CHEGOU AQUI")

            _uiState.value =
                _uiState.value.copy(
                    valor = "",
                    mensagem = UiMessage.Success(
                     "Despesa adicionada"
                    )
                )
            return@launch
        }
    }

    fun solicitarExclusao(
        despesa: DespesaEntity
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
                despesa
            )

            _uiState.value =
                _uiState.value.copy(
                    despesaSelecionadaParaExclusao = null,
                    mensagem = UiMessage.Success(
                     "Despesa excluída"
                    )
                )
        }
    }

    fun excluir(
        despesa: DespesaEntity
    ) {
        viewModelScope.launch {
            excluirDespesaUseCase(
                despesa
            )
        }
    }

    fun limparMensagem() {
        _uiState.value =
            _uiState.value.copy(
                mensagem = null
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
        tipo: TipoDespesaEntity
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
            finalizarFechamentoUseCase(
                fechamentoId
            )
            onSucesso()
        }
    }
}