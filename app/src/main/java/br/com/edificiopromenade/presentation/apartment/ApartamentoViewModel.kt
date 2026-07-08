package br.com.edificiopromenade.presentation.apartment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.edificiopromenade.domain.usecase.apartamento.AlterarApartamentoUseCase
import br.com.edificiopromenade.domain.usecase.apartamento.CadastrarApartamentoUseCase
import br.com.edificiopromenade.domain.usecase.apartamento.ConsultarApartamentoPorIdUseCase
import br.com.edificiopromenade.domain.usecase.apartamento.ConsultarApartamentosUiUseCase
import br.com.edificiopromenade.domain.usecase.apartamento.VerificarApartamentoExistenteUseCase
import br.com.edificiopromenade.domain.usecase.condominio.ConsultarCondominioAtivoUseCase
import br.com.edificiopromenade.presentation.apartment.mapper.toEntity
import br.com.edificiopromenade.presentation.apartment.model.ApartamentoUi
import br.com.edificiopromenade.presentation.common.message.UiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@HiltViewModel
class ApartamentoViewModel @Inject constructor(
    private val cadastrarApartamentoUseCase: CadastrarApartamentoUseCase,
    private val consultarCondominioAtivoUseCase: ConsultarCondominioAtivoUseCase,
    private val consultarApartamentosUiUseCase: ConsultarApartamentosUiUseCase,
    private val consultarApartamentoPorIdUseCase: ConsultarApartamentoPorIdUseCase,
    private val alterarApartamentoUseCase: AlterarApartamentoUseCase,
    private val verificarApartamentoExistenteUseCase: VerificarApartamentoExistenteUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            ApartamentoUiState()
        )

    val uiState = _uiState.asStateFlow()

    init {
        carregarApartamentos()
    }

    fun onNumeroChanged(
        valor: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                numero = valor
            )
    }

    fun onFracaoChanged(
        valor: String
    ) {
        val filtrado =
            valor.filter {
                it.isDigit() || it == ','
            }

        _uiState.value =
            _uiState.value.copy(
                percentualCopasa = filtrado
            )
    }

    fun salvar() {

        viewModelScope.launch(Dispatchers.IO) {

            val condominio = consultarCondominioAtivoUseCase()
                .first() ?: return@launch

            if (uiState.value.numero.isBlank()) {
                _uiState.value =
                    _uiState.value.copy(
                        mensagem = UiMessage.Error(
                            "Informe o número do apartamento."
                        )
                    )
                return@launch
            }

            val fracao =
                _uiState.value
                    .percentualCopasa
                    .replace(",", ".")
                    .toDoubleOrNull()


            if (fracao == null) {

                _uiState.value =
                    _uiState.value.copy(
                        mensagem = UiMessage.Error(
                            "Informe um percentual válido."
                        )
                    )
                return@launch
            }

            val apartamento = uiState.value.numero

            if (_uiState.value.modoEdicao) {

                alterarApartamentoUseCase(
                    ApartamentoUi(
                        id = uiState.value.apartamentoSelecionadoId,
                        condominioId = condominio.id,
                        numero = uiState.value.numero,
                        percentualCopasa = fracao,
                        ativo = true
                    ).toEntity()
                )

            } else {

                if (
                    verificarApartamentoExistenteUseCase(
                        numero = apartamento
                    )
                ) {
                    _uiState.value =
                        _uiState.value.copy(
                            mensagem = UiMessage.Error(
                                "O Apartamento '$apartamento' já foi cadastrado."
                            )
                        )
                    return@launch
                }

                cadastrarApartamentoUseCase(
                    ApartamentoUi(
                        condominioId = condominio.id,
                        numero = uiState.value.numero,
                        percentualCopasa = fracao,
                        ativo = true
                    ).toEntity()
                )
            }

            val mensagemSucesso =
                if (_uiState.value.modoEdicao)
                    "Apartamento atualizado com sucesso"
                else
                    "Apartamento cadastrado com sucesso"

            _uiState.value =
                _uiState.value.copy(
                    numero = "",
                    percentualCopasa = "",
                    apartamentoSelecionadoId = 0,
                    condominioIdSelecionado = 0,
                    modoEdicao = false,
                    salvoComSucesso = true,
                    mensagem = UiMessage.Success
                        (mensagemSucesso )
                )
        }
    }

    private fun carregarApartamentos() {

        viewModelScope.launch {

            consultarApartamentosUiUseCase()
                .collect { lista ->
                    _uiState.value =
                        _uiState.value.copy(
                            apartamentos = lista
                        )
                }
        }
    }

    fun cancelarEdicao() {

        _uiState.value =
            _uiState.value.copy(
                numero = "",
                percentualCopasa = "",
                apartamentoSelecionadoId = 0,
                condominioIdSelecionado = 0,
                modoEdicao = false,
                mensagem = UiMessage.Success
                    ("Alterações descartadas." )
            )
    }

    fun limparMensagem() {
        _uiState.value =
            _uiState.value.copy(
                mensagem = null
            )
    }

    fun selecionarApartamento(
        id: Long
    ) {
        viewModelScope.launch {

            val apartamento = consultarApartamentoPorIdUseCase(id)

            if (apartamento != null) {

                _uiState.value =
                    _uiState.value.copy(
                        apartamentoSelecionadoId = apartamento.id,
                        condominioIdSelecionado = apartamento.condominioId,
                        numero = apartamento.numero,
                        percentualCopasa = apartamento.percentualCopasa.toString(),
                        modoEdicao = true
                    )
            }
        }
    }
}