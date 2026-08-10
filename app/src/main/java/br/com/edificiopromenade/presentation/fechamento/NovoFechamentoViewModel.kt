package br.com.edificiopromenade.presentation.fechamento

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.edificiopromenade.data.local.entity.FechamentoMensalEntity
import br.com.edificiopromenade.domain.repository.FechamentoRepository
import br.com.edificiopromenade.domain.usecase.fechamento.ConsultarFechamentosUseCase
import br.com.edificiopromenade.domain.usecase.fechamento.CriarFechamentoMensalUseCase
import br.com.edificiopromenade.domain.usecase.fechamento.ConsultarFechamentoPorMesAnoUseCase
import br.com.edificiopromenade.domain.usecase.tipodespesa.PopularTiposDespesaUseCase
import br.com.edificiopromenade.presentation.common.message.UiMessage
import br.com.edificiopromenade.presentation.fechamento.mapper.toUi
import br.com.edificiopromenade.presentation.util.MoneyFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import jakarta.inject.Inject
import java.time.LocalDateTime
import java.time.YearMonth

@HiltViewModel
class NovoFechamentoViewModel @Inject constructor(
    private val criarFechamentoMensalUseCase: CriarFechamentoMensalUseCase,
    private val consultarFechamentoPorMesAnoUseCase: ConsultarFechamentoPorMesAnoUseCase,
    private val consultarFechamentosUseCase: ConsultarFechamentosUseCase,
    private val popularTiposDespesaUseCase: PopularTiposDespesaUseCase,
    private val fechamentoRepository: FechamentoRepository
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
                            fechamentos =
                                lista.map {
                                    it.toUi()
                                }
                        )
                }
        }
    }

    fun onFundoReservaChanged(
        valor: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                fundoReserva = valor
            )
    }

    fun onDecimoTerceiroChanged(
        valor: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                decimoTerceiroFerias = valor
            )
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
                    mensagem = UiMessage.Error(
                        "Mês deve estar entre 1 e 12"
                    )
                )
            return
        }

        if (!_uiState.value.modoEdicao) {

            val competenciaInformada =
                YearMonth.of(
                    ano,
                    mes
                )

            val competenciaAtual = YearMonth.now()

            val competenciaLimite = competenciaAtual.plusMonths(12)

            if (
                competenciaInformada.isBefore(
                    competenciaAtual
                )
            ) {
                _uiState.value =
                    _uiState.value.copy(
                        mensagem = UiMessage.Error(
                            "Não é permitido criar fechamento de competência anterior ao mês atual."
                        )
                    )

                return
            }

            if (
                competenciaInformada.isAfter(
                    competenciaLimite
                )
            ) {
                _uiState.value =
                    _uiState.value.copy(
                        mensagem = UiMessage.Error(
                            "O fechamento pode ser criado no máximo até 12 meses após a competência atual."
                        )
                    )

                return
            }
        }

        viewModelScope.launch {

            if (_uiState.value.modoEdicao) {

                val fechamentoAtual =
                    fechamentoRepository.findById(
                        _uiState.value.fechamentoSelecionadoId
                    )
                        ?: return@launch

                if (fechamentoAtual.finalizado) {

                    _uiState.value =
                        _uiState.value.copy(
                            mensagem = UiMessage.Error(
                                "Este fechamento já foi finalizado e não pode ser alterado."
                            )
                        )

                    return@launch
                }

                fechamentoRepository.update(

                    fechamentoAtual.copy(
                        mes = mes,
                        ano = ano,

                        valorFundoReserva =
                            MoneyFormatter.parse(
                                _uiState.value.fundoReserva
                            )?.toDouble()
                                ?: 0.0,

                        valorDecimoTerceiroFerias =
                            MoneyFormatter.parse(
                                _uiState.value.decimoTerceiroFerias
                            )?.toDouble()
                                ?: 0.0
                    )
                )

                _uiState.value =
                    _uiState.value.copy(
                        mes = "",
                        ano = "",
                        fundoReserva = "",
                        decimoTerceiroFerias = "",

                        modoEdicao = false,
                        fechamentoSelecionadoId = 0,
                        fechamentoSelecionadoFinalizado = false,
                        mensagem = UiMessage.Success(
                            "Fechamento atualizado com sucesso."
                        )
                    )

                return@launch
            }

            val fechamentoExistente = consultarFechamentoPorMesAnoUseCase(
                    mes,
                    ano
                )

            if (fechamentoExistente != null) {
                _uiState.value =
                    _uiState.value.copy(
                        mensagem = UiMessage.Error(
                            "Já existe fechamento para esta competência."
                        )
                    )
                return@launch
            }

            criarFechamentoMensalUseCase(
                FechamentoMensalEntity(

                    mes = mes,
                    ano = ano,

                    dataCriacao = LocalDateTime.now(),

                    valorFundoReserva =
                        MoneyFormatter.parse(
                            _uiState.value.fundoReserva
                            )
                            ?.toDouble()
                            ?: 0.0,

                    valorDecimoTerceiroFerias =
                        MoneyFormatter.parse(
                            _uiState.value.decimoTerceiroFerias
                            )
                            ?.toDouble()
                            ?: 0.0
                )
            )

            _uiState.value =
                _uiState.value.copy(
                    mes = "",
                    ano = "",
                    fundoReserva = "",
                    decimoTerceiroFerias = "",

                    modoEdicao = false,
                    fechamentoSelecionadoId = 0,
                    fechamentoSelecionadoFinalizado = false,

                    mensagem = UiMessage.Success(
                        "Fechamento criado com sucesso"
                    )
                )
        }
    }

    fun cancelarEdicao() {

        _uiState.value =
            _uiState.value.copy(
                mes = "",
                ano = "",
                fundoReserva = "",
                decimoTerceiroFerias = "",

                modoEdicao = false,
                fechamentoSelecionadoId = 0,
                fechamentoSelecionadoFinalizado = false,

                mensagem = null
            )
    }

    fun editarFechamento(
        fechamentoId: Long
    ) {
        viewModelScope.launch {

            val fechamento =
                fechamentoRepository.findById(
                    fechamentoId
                )
                    ?: return@launch

            _uiState.value =
                _uiState.value.copy(
                    mes = fechamento.mes
                            .toString()
                            .padStart(2, '0'),
                    ano = fechamento.ano.toString(),

                    fundoReserva =
                        MoneyFormatter.format(
                            (fechamento.valorFundoReserva * 100)
                                .toLong()
                                .toString()
                        ),

                    decimoTerceiroFerias =
                        MoneyFormatter.format(
                            (fechamento.valorDecimoTerceiroFerias * 100)
                                .toLong()
                                .toString()
                        ),

                    modoEdicao = true,
                    fechamentoSelecionadoId = fechamento.id,
                    fechamentoSelecionadoFinalizado = fechamento.finalizado,
                    mensagem = null
                )
        }
    }
}