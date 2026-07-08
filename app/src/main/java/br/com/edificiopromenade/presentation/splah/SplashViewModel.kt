package br.com.edificiopromenade.presentation.splah

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.edificiopromenade.domain.usecase.initialization.AppInitializationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val appInitializationUseCase: AppInitializationUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            SplashUiState()
        )

    val uiState = _uiState.asStateFlow()

    init {
        inicializar()
    }

    private fun inicializar() {

        viewModelScope.launch {
            val resultado = appInitializationUseCase()

            _uiState.value =
                SplashUiState(
                    loading = false,
                    initializationResult = resultado
                )
        }
    }
}