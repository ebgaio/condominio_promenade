package br.com.edificiopromenade.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.edificiopromenade.domain.navigation.EstadoSistema
import br.com.edificiopromenade.domain.usecase.navgacao.ValidarEstadoSistemaUseCase
import br.com.edificiopromenade.presentation.common.message.UiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val validarEstadoSistemaUseCase: ValidarEstadoSistemaUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())

    val uiState = _uiState.asStateFlow()

    private var estadoSistema: EstadoSistema? = null

    init {
        carregar()
    }

    fun carregar() {
        viewModelScope.launch {
            estadoSistema = validarEstadoSistemaUseCase()
        }
    }

    fun mostrarMensagem(texto: String) {
        _uiState.value =
            _uiState.value.copy(
                mensagem = UiMessage.Error(texto)
            )
    }

    fun limparMensagem() {
        _uiState.value =
            _uiState.value.copy(
                mensagem = null
            )
    }

    fun podeAbrirApartamentos(): Boolean {
        return estadoSistema?.possuiCondominio == true
    }

    fun podeAbrirMoradores(): Boolean {
        return estadoSistema?.possuiApartamento == true
    }

    fun podeAbrirNovoFechamento(): Boolean {
        return estadoSistema?.possuiMorador == true
    }

    fun mensagemMoradores(): String {
        return "Cadastre um condomínio antes de cadastrar apartamentos."
    }

    fun mensagemApartamentos(): String {
        return "Cadastre pelo menos um apartamento antes de cadastrar moradores."
    }

    fun mensagemNovoFechamento(): String {
        return "Cadastre apartamentos e moradores antes de iniciar um fechamento."
    }
}