package br.com.edificiopromenade.presentation.apartment.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.edificiopromenade.domain.usecase.apartamento.AlterarApartamentoUseCase
import br.com.edificiopromenade.domain.usecase.apartamento.ConsultarApartamentoDetalhadoUseCase
import br.com.edificiopromenade.domain.usecase.apartamento.InativarApartamentoUseCase
import br.com.edificiopromenade.presentation.common.message.UiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ApartamentoDetalheViewModel @Inject constructor(
    private val consultarApartamentoDetalhadoUseCase: ConsultarApartamentoDetalhadoUseCase,
    private val alterarApartamentoUseCase: AlterarApartamentoUseCase,
    private val inativarApartamentoUseCase: InativarApartamentoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
            ApartamentoDetalheUiState()
        )

    val uiState = _uiState.asStateFlow()

    fun carregar(
        id: Long
    ) {
        viewModelScope.launch {

            val apartamento = consultarApartamentoDetalhadoUseCase(id)

            val ativos = apartamento?.moradores
                    ?.count { it.ativo }
                    ?: 0

            val historico = apartamento?.moradores
                    ?.count { !it.ativo }
                    ?: 0

            _uiState.value =
                _uiState.value.copy(
                    carregando = false,
                    apartamento = apartamento,
                    totalMoradoresAtivos = ativos,
                    totalMoradoresHistorico = historico
                )
        }
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

    fun editar() {
        _uiState.value =
            _uiState.value.copy(
                numero = _uiState.value.apartamento?.apartamento?.numero ?: "",
                percentualCopasa = _uiState.value.apartamento?.apartamento?.percentualCopasa?.toString() ?: "",
                modoEdicao = true
            )
    }

    fun atualizar() {
        val detalhe = _uiState.value.apartamento
                ?: return

        if (_uiState.value.numero.isBlank()) {
            _uiState.value =
                _uiState.value.copy(
                    mensagem =
                        UiMessage.Error(
                            "Informe o número do apartamento."
                        )
                )
            return
        }

        val percentual =
            _uiState.value
                .percentualCopasa
                .replace(".", "")
                .replace(",", ".")
                .toDoubleOrNull()

        if (percentual == null) {

            _uiState.value =
                _uiState.value.copy(
                    mensagem =
                        UiMessage.Error(
                            "Informe um percentual válido."
                        )
                )

            return
        }

        viewModelScope.launch {
            alterarApartamentoUseCase(
                detalhe.apartamento.copy(

                    numero = _uiState.value.numero,
                    percentualCopasa =
                        _uiState.value
                            .percentualCopasa
                            .replace(".", "")
                            .replace(",", ".")
                            .toDoubleOrNull()
                            ?: 0.0
                )
            )

            carregar(
                detalhe.apartamento.id
            )

            _uiState.value =
                _uiState.value.copy(
                    modoEdicao = false,
                    mensagem = UiMessage.Success(
                            "Apartamento atualizado com sucesso."
                        )
                )
        }
    }

    fun inativarApartamento() {
        val id =
            _uiState.value
                .apartamento
                ?.apartamento
                ?.id
                ?: return

        viewModelScope.launch {
            inativarApartamentoUseCase(id)
            carregar(id)
        }

        _uiState.value =
            _uiState.value.copy(
                modoEdicao = false,
                mensagem = UiMessage.Success(
                    "Apartamento inativado com sucesso."
                )
            )
    }

    fun limparMensagem() {
        _uiState.value =
            _uiState.value.copy(
                mensagem = null
            )
    }
}