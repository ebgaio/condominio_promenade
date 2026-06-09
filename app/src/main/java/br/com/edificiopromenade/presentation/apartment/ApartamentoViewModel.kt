package br.com.edificiopromenade.presentation.apartment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.edificiopromenade.data.local.entity.ApartamentoEntity
import br.com.edificiopromenade.domain.usecase.apartamento.AlterarApartamentoUseCase
import br.com.edificiopromenade.domain.usecase.apartamento.CadastrarApartamentoUseCase
import br.com.edificiopromenade.domain.usecase.apartamento.ConsultarApartamentoPorIdUseCase
import br.com.edificiopromenade.domain.usecase.apartamento.ConsultarApartamentosUseCase
import br.com.edificiopromenade.domain.usecase.condominio.ConsultarCondominioAtivoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers

@HiltViewModel
class ApartamentoViewModel @Inject constructor(
    private val cadastrarApartamentoUseCase: CadastrarApartamentoUseCase,
    private val consultarCondominioAtivoUseCase: ConsultarCondominioAtivoUseCase,
    private val consultarApartamentosUseCase: ConsultarApartamentosUseCase,
    private val consultarApartamentoPorIdUseCase: ConsultarApartamentoPorIdUseCase,
    private val alterarApartamentoUseCase: AlterarApartamentoUseCase
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
        _uiState.value =
            _uiState.value.copy(
                fracaoIdealAtual = valor
            )
    }

    fun salvar() {

        viewModelScope.launch(Dispatchers.IO) {

            val condominio = consultarCondominioAtivoUseCase()
                    .first()

            if (condominio == null) {
                return@launch
            }

            val fracao =

                _uiState.value
                    .fracaoIdealAtual
                    .replace(",", ".")
                    .toDoubleOrNull()?: 0.0

            if (_uiState.value.modoEdicao) {

                alterarApartamentoUseCase(

                    ApartamentoEntity(
                        id = _uiState.value.apartamentoSelecionadoId,
                        condominioId = condominio.id,
                        numero = _uiState.value.numero,
                        fracaoIdealAtual = fracao,
                        ativo = true
                    )
                )

            } else {

                cadastrarApartamentoUseCase(

                    ApartamentoEntity(
                        condominioId = condominio.id,
                        numero = _uiState.value.numero,
                        fracaoIdealAtual = fracao,
                        ativo = true
                    )
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
                    fracaoIdealAtual = "",
                    apartamentoSelecionadoId = 0,
                    condominioIdSelecionado = 0,
                    modoEdicao = false,
                    salvoComSucesso = true,
                    mensagem = mensagemSucesso
                )
        }
    }

    private fun carregarApartamentos() {

        viewModelScope.launch {

            consultarApartamentosUseCase()
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
                fracaoIdealAtual = "",
                apartamentoSelecionadoId = 0,
                condominioIdSelecionado = 0,
                modoEdicao = false,
                mensagem = "Edição cancelada"
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
                        fracaoIdealAtual = apartamento.fracaoIdealAtual.toString(),
                        modoEdicao = true
                    )
            }
        }
    }
}