package br.com.edificiopromenade.presentation.moradores

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.edificiopromenade.domain.usecase.apartamento.ListarApartamentosComMoradoresUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MoradoresViewModel @Inject constructor(
    listarApartamentosComMoradoresUseCase: ListarApartamentosComMoradoresUseCase
) : ViewModel() {

    val uiState: StateFlow<MoradoresUiState> =
        listarApartamentosComMoradoresUseCase()
            .map { apartamentos ->

                MoradoresUiState(
                    apartamentos = apartamentos
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = MoradoresUiState(isLoading = true)
            )
}