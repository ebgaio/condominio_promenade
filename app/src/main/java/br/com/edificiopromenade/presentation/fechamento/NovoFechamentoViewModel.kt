package br.com.edificiopromenade.presentation.fechamento

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.edificiopromenade.data.local.entity.FechamentoMensalEntity
import br.com.edificiopromenade.domain.usecase.fechamento.ConsultarFechamentosUseCase
import br.com.edificiopromenade.domain.usecase.fechamento.CriarFechamentoMensalUseCase
import br.com.edificiopromenade.domain.usecase.fechamento.ConsultarFechamentoPorMesAnoUseCase
import br.com.edificiopromenade.domain.usecase.tipodespesa.PopularTiposDespesaUseCase
import br.com.edificiopromenade.presentation.common.message.UiMessage
import br.com.edificiopromenade.presentation.util.MoneyFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import jakarta.inject.Inject

@HiltViewModel
class NovoFechamentoViewModel @Inject constructor(

    private val criarFechamentoMensalUseCase: CriarFechamentoMensalUseCase,
    private val consultarFechamentoPorMesAnoUseCase: ConsultarFechamentoPorMesAnoUseCase,
    private val consultarFechamentosUseCase: ConsultarFechamentosUseCase,
    private val popularTiposDespesaUseCase: PopularTiposDespesaUseCase

) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            NovoFechamentoUiState()
        )

    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            popularTiposDespesaUseCase()
        }
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

    fun onFundoReservaChanged(
        valor: String
    ) {
        if (
            valor.matches(
                Regex("^\\d*([.,]\\d{0,2})?$")
            )
        ) {
            _uiState.value =
                _uiState.value.copy(
                    fundoReserva = MoneyFormatter.format(valor)
                )
        }
    }

    fun onDecimoTerceiroChanged(
        valor: String
    ) {
        if (
            valor.matches(
                Regex("^\\d*([.,]\\d{0,2})?$")
            )
        ) {
            _uiState.value =
                _uiState.value.copy(
                    decimoTerceiroFerias = MoneyFormatter.format(valor)
                )
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
        if (
            valor.length <= 4 &&
            valor.all { it.isDigit() }
        ) {
            _uiState.value =
                _uiState.value.copy(
                    ano = valor
                )
        }
    }

    fun limparMensagem() {
        _uiState.value =
            _uiState.value.copy(
                mensagem = null
            )
    }

    fun alterarExpandirMeses(
        expandido: Boolean
    ) {
        _uiState.value =
            _uiState.value.copy(
                expandirMeses = expandido
            )
    }

    fun selecionarMes(
        mes: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                mes = mes,
                expandirMeses = false
            )
    }

    fun salvar() {

        val mes = _uiState.value.mes.toIntOrNull()

        val ano = _uiState.value.ano.toIntOrNull()

        if (mes == null || ano == null)
            return

        if (mes !in 1..12) {

            _uiState.value =
                _uiState.value.copy(
                    mensagem = UiMessage.Success(
                        "Mês deve estar entre 1 e 12"
                    )
                )
            return
        }

        viewModelScope.launch {
            val fechamentoExistente = consultarFechamentoPorMesAnoUseCase(
                    mes,
                    ano
                )

            if (fechamentoExistente != null) {
                _uiState.value =
                    _uiState.value.copy(
                        mensagem = UiMessage.Success(
                            "Já existe fechamento para esta competência."
                        )
                    )
                return@launch
            }

            criarFechamentoMensalUseCase(
                FechamentoMensalEntity(

                    mes = mes,
                    ano = ano,

                    dataCriacao = java.time.LocalDateTime.now(),

                    valorFundoReserva =
                        _uiState.value.fundoReserva
                            .replace(".", "")
                            .replace(",", ".")
                            .toDoubleOrNull()
                            ?: 0.0,

                    valorDecimoTerceiroFerias =
                        _uiState.value.decimoTerceiroFerias
                            .replace(".", "")
                            .replace(",", ".")
                            .toDoubleOrNull()
                            ?: 0.0
                )
            )

            _uiState.value =
                _uiState.value.copy(
                    mes = "",
                    ano = "",
                    fundoReserva = "",
                    decimoTerceiroFerias = "",
                    mensagem = UiMessage.Success(
                        "Fechamento criado com sucesso"
                    )
                )
        }
    }
}