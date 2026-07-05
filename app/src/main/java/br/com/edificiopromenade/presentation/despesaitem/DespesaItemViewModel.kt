package br.com.edificiopromenade.presentation.despesaitem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.edificiopromenade.presentation.despesaitem.usecase.ConsultarItensDespesaUiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DespesaItemViewModel @Inject constructor(
    private val consultarItensDespesaUiUseCase: ConsultarItensDespesaUiUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            DespesaItemUiState()
        )

    val uiState = _uiState.asStateFlow()

    private var despesaId: Long = 0

    fun carregar(
        despesaId: Long
    ) {
        this.despesaId = despesaId

        viewModelScope.launch {
            consultarItensDespesaUiUseCase(
                despesaId
            ).collect { lista ->
                _uiState.value =
                    _uiState.value.copy(
                        itens = lista
                    )
            }
        }
    }
}