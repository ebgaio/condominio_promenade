package br.com.edificiopromenade.presentation.moradores

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.edificiopromenade.data.local.entity.MoradorEntity
import br.com.edificiopromenade.domain.usecase.apartamento.ListarApartamentosComMoradoresUseCase
import br.com.edificiopromenade.domain.usecase.morador.CadastrarMoradorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MoradoresViewModel @Inject constructor(
    private val cadastrarMoradorUseCase: CadastrarMoradorUseCase,
    private val listarApartamentosComMoradoresUseCase: ListarApartamentosComMoradoresUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            MoradoresUiState()
        )

    val uiState = _uiState.asStateFlow()

    init {
        carregarApartamentos()
    }

    private fun carregarApartamentos() {

        viewModelScope.launch {

            listarApartamentosComMoradoresUseCase()
                .collect { apartamentos ->

                    _uiState.value =
                        _uiState.value.copy(
                            apartamentos = apartamentos,
                            isLoading = false
                        )
                }
        }
    }

    fun onNomeChanged(    valor: String
    ) {

        _uiState.value =
            _uiState.value.copy(
                nome = valor
            )
    }

    fun onApartamentoSelecionado(
        apartamentoId: Long
    ) {

        _uiState.value =
            _uiState.value.copy(
                apartamentoIdSelecionado = apartamentoId
            )
    }

    fun salvar() {
        viewModelScope.launch {

            if (_uiState.value.nome.isBlank()) {

//                _uiState.value =
//                    _uiState.value.copy(
//                        error = "Informe o nome do morador."
//                    )

                return@launch
            }
            if (_uiState.value.apartamentoIdSelecionado == 0L) {

//                _uiState.value =
//                    _uiState.value.copy(
//                        error = "Selecione um apartamento."
//                    )

                return@launch
            }

            cadastrarMoradorUseCase(
                MoradorEntity(
                    apartamentoId = _uiState.value.apartamentoIdSelecionado,
                    nome = _uiState.value.nome,
                    dataInicio = LocalDate.now(),
                    dataFim = null,
                    ativo = true
                )
            )

            _uiState.value =
                _uiState.value.copy(
                    nome = "",
                    apartamentoIdSelecionado = 0,
                    salvoComSucesso = true
                )
        }
    }
}