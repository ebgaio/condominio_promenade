package br.com.edificiopromenade.presentation.moradores

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.edificiopromenade.domain.usecase.morador.AlterarMoradorUseCase
import br.com.edificiopromenade.domain.usecase.morador.CadastrarMoradorUseCase
import br.com.edificiopromenade.domain.usecase.morador.ConsultarMoradorPorIdUiUseCase
import br.com.edificiopromenade.domain.usecase.morador.EncerrarMoradorUseCase
import br.com.edificiopromenade.domain.usecase.morador.ListarApartamentosComMoradoresUiUseCase
import br.com.edificiopromenade.presentation.common.message.UiMessage
import br.com.edificiopromenade.presentation.resident.mapper.toEntity
import br.com.edificiopromenade.presentation.resident.model.MoradorUi
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

@HiltViewModel
class MoradoresViewModel @Inject constructor(
    private val cadastrarMoradorUseCase: CadastrarMoradorUseCase,
    private val consultarMoradorPorIdUiUseCase: ConsultarMoradorPorIdUiUseCase,
    private val alterarMoradorUseCase: AlterarMoradorUseCase,
    private val listarApartamentosComMoradoresUiUseCase: ListarApartamentosComMoradoresUiUseCase,
    private val encerrarMoradorUseCase : EncerrarMoradorUseCase
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

        _uiState.value =
            _uiState.value.copy(
                isLoading = true
            )

        viewModelScope.launch {
            listarApartamentosComMoradoresUiUseCase()
                .collect { apartamentos ->
                    _uiState.value =
                        _uiState.value.copy(
                            apartamentos = apartamentos,
                            isLoading = false
                        )
                }
        }

        _uiState.value =
            _uiState.value.copy(
                isLoading = false
            )
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

    fun selecionarMorador(
        id: Long
    ) {
        viewModelScope.launch {

            val morador = consultarMoradorPorIdUiUseCase(id)

            if (morador != null) {

                _uiState.value =
                    _uiState.value.copy(
                        moradorSelecionadoId = morador.id,
                        apartamentoIdSelecionado = morador.apartamentoId,
                        nome = morador.nome,
                        modoEdicao = true,
                        mensagem = null
                    )
            }
        }
    }

    fun encerrarMorador() {
        val id = uiState.value.moradorSelecionadoId

        if (id == 0L)
            return

        viewModelScope.launch {
            encerrarMoradorUseCase(
                id,
                LocalDate.now()
            )
            carregarApartamentos()
            cancelarEdicao()

            _uiState.value =
                _uiState.value.copy(
                    mensagem = UiMessage.Success(
                        "Morador encerrado com sucesso."
                    )
                )
        }
    }

    fun cancelarEdicao()
    {
        limparFormulario()
        _uiState.value =
            _uiState.value.copy(
                mensagem = UiMessage.Success(
                    "Alterações descartadas."
                )
            )
    }

    private suspend fun cadastrarMorador() {
        cadastrarMoradorUseCase(
            MoradorUi(
                apartamentoId = uiState.value.apartamentoIdSelecionado,
                nome = uiState.value.nome,
                dataInicio = LocalDate.now(),
                ativo = true
            ).toEntity()
        )
    }

    private suspend fun atualizarMorador() {
        val morador =
            consultarMoradorPorIdUiUseCase(
                uiState.value.moradorSelecionadoId
            ) ?: return

        alterarMoradorUseCase(
            morador.copy(
                nome = uiState.value.nome,
                apartamentoId =
                    uiState.value.apartamentoIdSelecionado
            ).toEntity()
        )
    }

    fun salvar() {
        if (!validarFormulario())
            return

        viewModelScope.launch {
            if (uiState.value.modoEdicao) {
                atualizarMorador()
                carregarApartamentos()
            }
            else {
                cadastrarMorador()
                carregarApartamentos()
            }
            finalizarOperacao()
        }
    }

    private fun validarFormulario(): Boolean {

        if (uiState.value.nome.isBlank()) {
            _uiState.value =
                _uiState.value.copy(
                    mensagem =
                        UiMessage.Error(
                            "Informe o nome do morador."
                        )
                )
            return false
        }

        if (uiState.value.apartamentoIdSelecionado == 0L) {
            _uiState.value =
                _uiState.value.copy(
                    mensagem =
                        UiMessage.Error(
                            "Selecione um apartamento."
                        )
                )
            return false
        }
        return true
    }

    private fun finalizarOperacao() {
        limparFormulario()
        _uiState.value =
            _uiState.value.copy(
                salvoComSucesso = true,
                mensagem = UiMessage.Success(textoSucesso())
            )
    }

    private fun textoSucesso() =
        if (uiState.value.modoEdicao)
            "Morador atualizado com sucesso."
        else
            "Morador cadastrado com sucesso."

    private fun limparFormulario() {
        _uiState.value =
            _uiState.value.copy(
                nome = "",
                apartamentoIdSelecionado = 0,
                moradorSelecionadoId = 0,
                modoEdicao = false
            )
    }

    fun limparMensagem() {
        _uiState.value =
            _uiState.value.copy(
                mensagem = null
            )
    }
}