package br.com.edificiopromenade.presentation.condominio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.edificiopromenade.domain.usecase.condominio.CadastrarCondominioUseCase
import br.com.edificiopromenade.domain.usecase.condominio.ConsultarCondominioAtivoUseCase
import br.com.edificiopromenade.presentation.common.message.UiMessage
import br.com.edificiopromenade.presentation.condominio.mapper.toEntity
import br.com.edificiopromenade.presentation.condominio.mapper.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class CondominioViewModel @Inject constructor(
    private val cadastrarCondominioUseCase: CadastrarCondominioUseCase,
    private val consultarCondominioAtivoUseCase: ConsultarCondominioAtivoUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            CondominioUiState()
        )

    val uiState: StateFlow<CondominioUiState> = _uiState.asStateFlow()

    init {
        carregar()
    }

    fun onNomeChanged(
        valor: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                condominio =
                    _uiState.value.condominio.copy(
                        nome = valor
                    )
            )
    }

    fun onCnpjChanged(
        cnpj: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                condominio =
                    _uiState.value.condominio.copy(
                        cnpj = cnpj.filter {
                            it.isDigit()
                        }
                        .take(14)
                    )
            )
    }

    fun onEnderecoChanged(
        valor: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                condominio =
                    _uiState.value.condominio.copy(
                        endereco = valor
                    )
            )
    }

    fun onNomeAdministradoraChanged(
        valor: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                condominio =
                    _uiState.value.condominio.copy(
                        nomeAdministradora = valor
                    )
            )
    }

    fun onEmailAdministradoraChanged(
        valor: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                condominio =
                    _uiState.value.condominio.copy(
                        emailAdministradora = valor
                    )
            )
    }

    fun salvar() {

        viewModelScope.launch {

            val somenteNumeros =
                _uiState.value.condominio.cnpj.filter {
                    it.isDigit()
                }

            if (somenteNumeros.length != 14) {
                _uiState.value =
                    _uiState.value.copy(
                        mensagem = UiMessage.Error(
                            "CNPJ inválido."
                        ),
                    )
                return@launch
            }

            val novoId = cadastrarCondominioUseCase(
            _uiState.value
                .condominio
                .toEntity()
            )

            _uiState.value =
                _uiState.value.copy(
                    mensagem = UiMessage.Success(
                        "Condomínio salvo com sucesso."
                    )
                )
        }
    }

    fun limparMensagem() {
        _uiState.value =
            _uiState.value.copy(
                mensagem = null
            )
    }

    private fun carregar() {

        viewModelScope.launch {

            val condominio = consultarCondominioAtivoUseCase()
                .collectLatest { condominio ->
                    condominio?.let {
                        _uiState.value =
                            _uiState.value.copy(
                                condominio = it.toUi()
                            )
                    }
            }
        }
    }
}