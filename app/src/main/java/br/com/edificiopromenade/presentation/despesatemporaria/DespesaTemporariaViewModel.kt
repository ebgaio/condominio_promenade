package br.com.edificiopromenade.presentation.despesatemporaria

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.edificiopromenade.domain.usecase.despesatemporaria.AssociarParcelasAoFechamentoUseCase
import br.com.edificiopromenade.domain.usecase.despesatemporaria.CadastrarDespesaTemporariaParcelaUseCase
import br.com.edificiopromenade.domain.usecase.despesatemporaria.CadastrarDespesaTemporariaUseCase
import br.com.edificiopromenade.domain.usecase.fechamento.ConsultarFechamentoPorIdUseCase
import br.com.edificiopromenade.domain.usecase.tipodespesa.VerificarNomeReservadoTipoDespesaUseCase
import br.com.edificiopromenade.presentation.common.message.UiMessage
import br.com.edificiopromenade.presentation.despesatemporaria.mapper.toEntity
import br.com.edificiopromenade.presentation.despesatemporaria.model.DespesaTemporariaParcelaUi
import br.com.edificiopromenade.presentation.util.MoneyFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.YearMonth

@HiltViewModel
class DespesaTemporariaViewModel @Inject constructor(
    private val cadastrarDespesaTemporariaUseCase: CadastrarDespesaTemporariaUseCase,
    private val cadastrarParcelaUseCase: CadastrarDespesaTemporariaParcelaUseCase,
    private val consultarFechamentoPorIdUseCase: ConsultarFechamentoPorIdUseCase,
    private val associarParcelasAoFechamentoUseCase: AssociarParcelasAoFechamentoUseCase,
    private val verificarNomeReservadoTipoDespesaUseCase: VerificarNomeReservadoTipoDespesaUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            DespesaTemporariaUiState()
        )

    val uiState = _uiState.asStateFlow()

    fun onDescricaoChanged(
        valor: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                descricao = valor
            )
    }

    fun onQuantidadeParcelasChanged(
        valor: String
    ) {

        if (
            valor.all { it.isDigit() } ||
            valor.isEmpty()
        ) {
            _uiState.value =
                _uiState.value.copy(
                    quantidadeParcelas = valor
                )

            val quantidade = valor.toIntOrNull() ?: 0
            val lista = MutableList(quantidade) { "" }

            _uiState.value =
                _uiState.value.copy(
                    parcelas = lista
                )
        }
    }

    fun onValorParcelaChanged(
        indice: Int,
        valor: String
    ) {
        val lista =
            _uiState.value
                .parcelas
                .toMutableList()

        lista[indice] = valor

        _uiState.value =
            _uiState.value.copy(
                parcelas = lista
            )
    }

    fun adicionarParcela() {

        val lista =
            _uiState.value
                .parcelas
                .toMutableList()

        lista.add("")

        _uiState.value =
            _uiState.value.copy(
                parcelas = lista,
                quantidadeParcelas =
                    lista.size.toString()

            )
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

    fun selecionarMesInicial(
        mes: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                mesInicial = mes,
                expandirMeses = false
            )
    }

    fun onAnoInicialChanged(
        valor: String
    ) {
        if (
            valor.length <= 4 &&
            valor.all(Char::isDigit)
        ) {
            _uiState.value =
                _uiState.value.copy(
                    anoInicial = valor
                )
        }
    }

    fun carregar(
        fechamentoId: Long
    ) {

        viewModelScope.launch {
            val fechamento =
                consultarFechamentoPorIdUseCase(
                    fechamentoId
                )
                    ?: return@launch

            _uiState.value =
                _uiState.value.copy(
                    competencia = "${
                        fechamento.mes
                            .toString()
                            .padStart(2, '0')
                    }/${fechamento.ano}",
                    mesInicial =
                        if (
                            _uiState.value.mesInicial.isBlank()
                        ) {
                            fechamento.mes
                                .toString()
                                .padStart(2, '0')
                        } else {
                            _uiState.value.mesInicial
                        },

                    anoInicial =
                        if (
                            _uiState.value.anoInicial.isBlank()
                        ) {
                            fechamento.ano.toString()
                        } else {
                            _uiState.value.anoInicial
                        }
                )
        }
    }

    fun salvar(
        fechamentoId: Long,
        onSucesso: () -> Unit
    ) {

        /*
         * Evita duplo clique enquanto
         * uma gravação já estiver ocorrendo.
         */
        if (_uiState.value.carregando) {
            return
        }

        viewModelScope.launch {

            val estado = _uiState.value

            val descricao = _uiState.value.descricao.trim()

            /*
             * =====================================================
             * DESCRIÇÃO OBRIGATÓRIA
             * =====================================================
             */
            if (descricao.isBlank()) {
                _uiState.value =
                    _uiState.value.copy(
                        mensagem =
                            UiMessage.Error(
                                "Informe a descrição."
                            )
                    )
                return@launch
            }

            /*
             * =====================================================
             * NOME RESERVADO PARA DESPESA FIXA
             * =====================================================
             *
             * Como este UseCase é suspend, ele precisa
             * estar dentro do viewModelScope.launch.
             */
            if (
                verificarNomeReservadoTipoDespesaUseCase(
                    descricao
                )
            ) {

                _uiState.value =
                    _uiState.value.copy(
                        mensagem = UiMessage.Error(
                            "O nome '$descricao' pertence a uma despesa fixa e não pode ser usado em uma despesa avulsa."
                        )
                    )

                return@launch
            }

            /*
             * =====================================================
             * COMPETÊNCIA INICIAL
             * =====================================================
             */
            val mesInicial = estado.mesInicial.toIntOrNull()

            val anoInicial = estado.anoInicial.toIntOrNull()

            if (
                mesInicial == null ||
                mesInicial !in 1..12 ||
                anoInicial == null ||
                estado.anoInicial.length != 4
            ) {

                _uiState.value =
                    _uiState.value.copy(
                        mensagem = UiMessage.Error(
                            "Informe uma competência inicial válida."
                        )
                    )

                return@launch
            }

            val competenciaInicial =
                YearMonth.of(
                    anoInicial,
                    mesInicial
                )

            val competenciaAtual = YearMonth.now()

            val competenciaLimite = competenciaAtual.plusMonths(12)

            /*
             * Não permite competência anterior
             * ao mês atual.
             */
            if (
                competenciaInicial.isBefore(
                    competenciaAtual
                )
            ) {
                _uiState.value =
                    _uiState.value.copy(
                        mensagem = UiMessage.Error(
                            "Não é permitido criar fechamento de competência anterior ao mês atual."
                        )
                    )

                return@launch
            }

            /*
             * Limite máximo de 12 meses.
             */
            if (
                competenciaInicial.isAfter(
                    competenciaLimite
                )
            ) {
                _uiState.value =
                    _uiState.value.copy(
                        mensagem = UiMessage.Error(
                            "O fechamento pode ser criado no máximo até 12 meses após a competência atual."
                        )
                    )

                return@launch
            }

            /*
             * =====================================================
             * PARCELAS
             * =====================================================
             */
            if (estado.parcelas.isEmpty()) {

                _uiState.value =
                    _uiState.value.copy(
                        mensagem = UiMessage.Error(
                            "Informe pelo menos uma parcela."
                        )
                    )

                return@launch
            }

            val valoresParcelas =
                estado.parcelas.map { valorTexto ->

                    MoneyFormatter.parse(
                        valorTexto
                    )?.toDouble()
                }

            if (
                valoresParcelas.any {
                    it == null || it <= 0.0
                }
            ) {

                _uiState.value =
                    _uiState.value.copy(
                        mensagem = UiMessage.Error(
                            "Informe um valor válido para todas as parcelas."
                        )
                    )

                return@launch
            }

            /*
             * =====================================================
             * GRAVAÇÃO
             * =====================================================
             */
            _uiState.value =
                _uiState.value.copy(
                    carregando = true,
                    mensagem = null
                )

            try {

                val despesaId =
                    cadastrarDespesaTemporariaUseCase(
                        descricao = descricao,
                        quantidadeParcelas =
                            valoresParcelas.size
                    )

                valoresParcelas.forEachIndexed { indice,
                                                 valor ->

                    val competenciaParcela =
                        competenciaInicial.plusMonths(
                            indice.toLong()
                        )

                    val competencia = competenciaParcela.year * 100 + competenciaParcela.monthValue

                    cadastrarParcelaUseCase(
                        DespesaTemporariaParcelaUi(
                            despesaTemporariaId = despesaId,
                            competencia = competencia,
                            numeroParcela = indice + 1,
                            valor = valor!!
                        ).toEntity()
                    )
                }

                /*
                 * O fechamento já existe.
                 *
                 * Depois da criação das parcelas,
                 * associa a parcela correspondente
                 * à competência deste fechamento.
                 */
                val fechamento =
                    consultarFechamentoPorIdUseCase(
                        fechamentoId
                    )

                if (fechamento != null) {

                    val competenciaFechamento = fechamento.ano * 100 + fechamento.mes

                    associarParcelasAoFechamentoUseCase(
                        competencia = competenciaFechamento,
                        fechamentoId = fechamentoId
                    )
                }

                _uiState.value =
                    DespesaTemporariaUiState(
                        mensagem = UiMessage.Success(
                            "Despesa avulsa cadastrada com sucesso."
                        )
                    )

                onSucesso()

            } catch (
                exception: IllegalArgumentException
            ) {
                _uiState.value =
                    _uiState.value.copy(
                        carregando = false,
                        mensagem = UiMessage.Error(
                            exception.message
                                ?: "Não foi possível cadastrar a despesa."
                        )
                    )

            } catch (
                exception: Exception
            ) {
                _uiState.value =
                    _uiState.value.copy(
                        carregando = false,
                        mensagem = UiMessage.Error(
                            "Não foi possível cadastrar a despesa avulsa."
                        )
                    )
            }
        }
    }
}
