package br.com.edificiopromenade.presentation.apartment.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.edificiopromenade.domain.usecase.apartamento.AlterarApartamentoUseCase
import br.com.edificiopromenade.domain.usecase.apartamento.ConsultarApartamentoDetalhadoUseCase
import br.com.edificiopromenade.domain.usecase.apartamento.InativarApartamentoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import jakarta.inject.Inject

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

    private val _mensagem = MutableStateFlow<String?>(null)

    val mensagem = _mensagem.asStateFlow()

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
        _uiState.value =
            _uiState.value.copy(
                fracaoIdealAtual = valor
            )
    }

    fun editar() {
        _uiState.value =
            _uiState.value.copy(
                modoEdicao = true
            )
    }

    fun atualizar() {
        val detalhe = _uiState.value.apartamento
                ?: return

        viewModelScope.launch {
            alterarApartamentoUseCase(
                detalhe.apartamento.copy(

                    numero = _uiState.value.numero,
                    fracaoIdealAtual =
                        _uiState.value
                            .fracaoIdealAtual
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
                    mensagem = "Apartamento atualizado com sucesso"
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
            _mensagem.value = "Apartamento inativado com sucesso"
            carregar(id)
        }
    }

    fun limparMensagem() {
        _mensagem.value = null
    }
}