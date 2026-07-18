package br.com.edificiopromenade.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.edificiopromenade.domain.navigation.EstadoSistema
import br.com.edificiopromenade.domain.usecase.navgacao.ValidarEstadoSistemaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val validarEstadoSistemaUseCase: ValidarEstadoSistemaUseCase
) : ViewModel() {

    private var estadoSistema: EstadoSistema? = null

    init {
        carregar()
    }

    fun carregar() {

        viewModelScope.launch {
            estadoSistema = validarEstadoSistemaUseCase()
        }
    }

}