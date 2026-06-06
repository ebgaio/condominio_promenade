package br.com.edificiopromenade.presentation.apartment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.edificiopromenade.data.local.entity.ApartamentoEntity
import br.com.edificiopromenade.domain.usecase.apartamento.CadastrarApartamentoUseCase
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
    private val consultarApartamentosUseCase: ConsultarApartamentosUseCase
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

            cadastrarApartamentoUseCase(

                ApartamentoEntity(

                    condominioId = condominio.id,

                    numero =_uiState.value.numero,

                    fracaoIdealAtual = fracao,

                    ativo = true
                )
            )

            _uiState.value =
                _uiState.value.copy(
                    numero = "",
                    fracaoIdealAtual = "",
                    salvoComSucesso = true
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
}