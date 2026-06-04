package br.com.edificiopromenade.presentation.condominio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.edificiopromenade.data.local.entity.CondominioEntity
import br.com.edificiopromenade.domain.usecase.condominio.CadastrarCondominioUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class CondominioViewModel @Inject constructor(
    private val cadastrarCondominioUseCase:
        CadastrarCondominioUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            CondominioUiState()
        )

    val uiState:
            StateFlow<CondominioUiState> =
        _uiState.asStateFlow()

    fun onNomeChanged(
        valor: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                nome = valor
            )
    }

    fun onCnpjChanged(
        valor: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                cnpj = valor
            )
    }

    fun onEnderecoChanged(
        valor: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                endereco = valor
            )
    }

    fun onNomeAdministradoraChanged(
        valor: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                nomeAdministradora = valor
            )
    }

    fun onEmailAdministradoraChanged(
        valor: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                emailAdministradora = valor
            )
    }

    fun salvar() {

        viewModelScope.launch {

            cadastrarCondominioUseCase(

                CondominioEntity(

                    nome =
                        _uiState.value.nome,

                    cnpj =
                        _uiState.value.cnpj,

                    endereco =
                        _uiState.value.endereco,

                    nomeAdministradora =
                        _uiState.value.nomeAdministradora,

                    emailAdministradora =
                        _uiState.value.emailAdministradora,

                    ativo = true,

                    dataCriacao =
                        LocalDateTime.now()
                )
            )

            _uiState.value =
                _uiState.value.copy(
                    salvoComSucesso = true
                )
        }
    }
}