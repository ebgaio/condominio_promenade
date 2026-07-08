package br.com.edificiopromenade.presentation.initialization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.edificiopromenade.domain.model.AppInitializationResult
import br.com.edificiopromenade.domain.usecase.initialization.AppInitializationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class InitializationViewModel @Inject constructor(
    private val appInitializationUseCase: AppInitializationUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            InitializationUiState()
        )

    val uiState = _uiState.asStateFlow()

    init {
        verificarInicializacao()
    }

    fun verificarInicializacao() {

        viewModelScope.launch {

            val resultado = appInitializationUseCase()

            val step = when (resultado) {
                AppInitializationResult.NeedCondominio -> InitializationStep.CONDOMINIO
                AppInitializationResult.NeedApartamento -> InitializationStep.APARTAMENTOS
                AppInitializationResult.NeedMorador -> InitializationStep.MORADORES
                AppInitializationResult.Ready -> InitializationStep.HOME
            }

            _uiState.value =
                InitializationUiState(
                    loading = false,
                    step = step
                )
        }
    }
}