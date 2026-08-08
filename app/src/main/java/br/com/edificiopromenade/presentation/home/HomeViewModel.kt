package br.com.edificiopromenade.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.edificiopromenade.domain.usecase.navegacao.ValidarEstadoSistemaUseCase
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

    init {
        carregar()
    }

    fun carregar() {
        viewModelScope.launch {
            _uiState.value =
                _uiState.value.copy(
                    carregando = true
                )

            try {
                val estado = validarEstadoSistemaUseCase()

            _uiState.value =
                _uiState.value.copy(
                    estadoSistema = estado,
                    carregando = false
                )
            } catch (e: Exception) {
                _uiState.value =
                    _uiState.value.copy(
                        mensagem = UiMessage.Error(
                            "Não foi possível verificar o estado do sistema."
                        )
                    )

            } finally {

                _uiState.value =
                    _uiState.value.copy(
                        carregando = false
                    )
            }
        }
    }

    fun podeAbrirApartamentos(): Boolean {
        return _uiState.value
            .estadoSistema
            ?.possuiCondominio == true
    }

    fun podeAbrirMoradores(): Boolean {
        return _uiState.value
            .estadoSistema
            ?.possuiApartamento == true
    }

    fun podeAbrirNovoFechamento(): Boolean {
        val estado = _uiState.value
            .estadoSistema
            ?: return false
        return estado.possuiCondominio &&
                estado.possuiApartamento &&
                estado.possuiMorador
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

    fun mensagemMoradores(): String {
        return "Cadastre pelo menos um apartamento antes de cadastrar moradores."
    }

    fun mensagemApartamentos(): String {
        return "Cadastre um condomínio antes de cadastrar apartamentos."
    }

    fun mensagemNovoFechamento(): String {
        return "Cadastre apartamentos e moradores antes de iniciar um fechamento."
    }
}