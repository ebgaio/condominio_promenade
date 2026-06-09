package br.com.edificiopromenade.presentation.moradores

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.edificiopromenade.data.local.entity.MoradorEntity
import br.com.edificiopromenade.domain.usecase.apartamento.ListarApartamentosComMoradoresUseCase
import br.com.edificiopromenade.domain.usecase.morador.AlterarMoradorUseCase
import br.com.edificiopromenade.domain.usecase.morador.CadastrarMoradorUseCase
import br.com.edificiopromenade.domain.usecase.morador.ConsultarMoradorPorIdUseCase
import br.com.edificiopromenade.domain.usecase.morador.EncerrarMoradorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class MoradoresViewModel @Inject constructor(
    private val cadastrarMoradorUseCase: CadastrarMoradorUseCase,
    private val consultarMoradorPorIdUseCase: ConsultarMoradorPorIdUseCase,
    private val alterarMoradorUseCase: AlterarMoradorUseCase,
    private val listarApartamentosComMoradoresUseCase: ListarApartamentosComMoradoresUseCase,
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

        viewModelScope.launch {

            listarApartamentosComMoradoresUseCase()
                .collect { apartamentos ->

                    _uiState.value =
                        _uiState.value.copy(
                            apartamentos = apartamentos,
                            isLoading = false
                        )
                }
        }
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

            val morador = consultarMoradorPorIdUseCase(id)

            if (morador != null) {

                _uiState.value =
                    _uiState.value.copy(
                        moradorSelecionadoId = morador.id,
                        apartamentoIdSelecionado = morador.apartamentoId,
                        nome = morador.nome,
                        modoEdicao = true
                    )
            }
        }
    }

    fun encerrarMorador() {

        if (_uiState.value.moradorSelecionadoId == 0L)
            return

        viewModelScope.launch {

                encerrarMoradorUseCase (
                    _uiState.value.moradorSelecionadoId,
                    LocalDate.now()
                )

                _uiState.value =
                    _uiState.value.copy(
                        nome = "",
                        apartamentoIdSelecionado = 0,
                        moradorSelecionadoId = 0,
                        modoEdicao = false,
                        mensagem = "Morador encerrado"
                    )
        }
    }

    fun cancelarEdicao()
    {
        _uiState.value =
            _uiState.value.copy(
                nome = "",
                apartamentoIdSelecionado = 0,
                moradorSelecionadoId = 0,
                modoEdicao = false
            )
    }

    fun salvar() {

        if (_uiState.value.nome.isBlank()) {
            return
        }

        if (_uiState.value.apartamentoIdSelecionado == 0L) {
            return
        }

        viewModelScope.launch {

            if (_uiState.value.modoEdicao) {

                val morador = consultarMoradorPorIdUseCase(
                        _uiState.value.moradorSelecionadoId
                    )

                if (morador != null) {

                    alterarMoradorUseCase(
                        morador.copy(
                            nome = _uiState.value.nome,
                            apartamentoId = _uiState.value.apartamentoIdSelecionado
                        )
                    )
                }
            } else {

                cadastrarMoradorUseCase(
                    MoradorEntity(
                        apartamentoId = _uiState.value.apartamentoIdSelecionado,
                        nome = _uiState.value.nome,
                        dataInicio = LocalDate.now(),
                        dataFim = null,
                        ativo = true
                    )
                )
            }

            val mensagemSucesso =
                if (_uiState.value.modoEdicao)
                    "Morador atualizado com sucesso"
                else
                    "Morador cadastrado com sucesso"

            _uiState.value =
                _uiState.value.copy(
                    nome = "",
                    apartamentoIdSelecionado = 0,
                    moradorSelecionadoId = 0,
                    modoEdicao = false,
                    salvoComSucesso = true,
                    mensagem = mensagemSucesso
                )
        }
    }
}