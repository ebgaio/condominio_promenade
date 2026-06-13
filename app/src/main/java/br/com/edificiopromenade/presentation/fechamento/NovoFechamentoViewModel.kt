package br.com.edificiopromenade.presentation.fechamento

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.edificiopromenade.data.local.entity.FechamentoMensalEntity
import br.com.edificiopromenade.domain.usecase.fechamento.ConsultarFechamentosUseCase
import br.com.edificiopromenade.domain.usecase.fechamento.CriarFechamentoMensalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NovoFechamentoViewModel @Inject constructor(

    private val criarFechamentoMensalUseCase: CriarFechamentoMensalUseCase,

    private val consultarFechamentosUseCase: ConsultarFechamentosUseCase

) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            NovoFechamentoUiState()
        )

    val uiState = _uiState.asStateFlow()

    init {
        carregarFechamentos()
    }

    private fun carregarFechamentos() {

        viewModelScope.launch {

            consultarFechamentosUseCase()
                .collect { lista ->

                    _uiState.value =
                        _uiState.value.copy(
                            fechamentos = lista
                        )
                }
        }
    }

    fun onMesChanged(
        valor: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                mes = valor
            )
    }

    fun onAnoChanged(
        valor: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                ano = valor
            )
    }

    fun limparMensagem() {

        _uiState.value =
            _uiState.value.copy(
                mensagem = null
            )
    }

    fun salvar() {

        val mes = _uiState.value.mes.toIntOrNull()

        val ano = _uiState.value.ano.toIntOrNull()

        if (mes == null || ano == null)
            return

        viewModelScope.launch {
            criarFechamentoMensalUseCase(
                FechamentoMensalEntity(

                    mes = mes,
                    ano = ano,

                    dataCriacao = java.time.LocalDateTime.now()
                )
            )

            _uiState.value =
                _uiState.value.copy(
                    mes = "",
                    ano = "",
                    mensagem = "Fechamento criado com sucesso"
                )
        }
    }
}