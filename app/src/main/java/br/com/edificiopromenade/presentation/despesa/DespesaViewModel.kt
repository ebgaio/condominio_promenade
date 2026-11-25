package br.com.edificiopromenade.presentation.despesa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.edificiopromenade.domain.model.AtualizarDespesaCommand
import br.com.edificiopromenade.domain.model.CadastrarDespesaCommand
import br.com.edificiopromenade.domain.model.ExcluirDespesaCommand
import br.com.edificiopromenade.domain.repository.DespesaRepository
import br.com.edificiopromenade.domain.repository.DespesaTemporariaParcelaRepository
import br.com.edificiopromenade.domain.repository.DespesaTemporariaRepository
import br.com.edificiopromenade.domain.repository.FechamentoRepository
import br.com.edificiopromenade.domain.usecase.despesa.AtualizarDespesaUseCase
import br.com.edificiopromenade.domain.usecase.despesa.CadastrarDespesaUseCase
import br.com.edificiopromenade.domain.usecase.despesa.ConsultarDespesasPorFechamentoUseCase
import br.com.edificiopromenade.domain.usecase.despesa.ExcluirDespesaUseCase
import br.com.edificiopromenade.domain.usecase.despesa.ExcluirParcelaDespesaAvulsaUseCase
import br.com.edificiopromenade.domain.usecase.despesa.VerificarDespesaExistenteUseCase
import br.com.edificiopromenade.domain.usecase.fechamento.FinalizarFechamentoUseCase
import br.com.edificiopromenade.domain.usecase.tipodespesa.ConsultarTiposDespesaUiUseCase
import br.com.edificiopromenade.domain.usecase.tipodespesa.VerificarNomeReservadoTipoDespesaUseCase
import br.com.edificiopromenade.presentation.common.message.UiMessage
import br.com.edificiopromenade.presentation.despesa.mapper.toUi
import br.com.edificiopromenade.presentation.despesa.model.DespesaUi
import br.com.edificiopromenade.presentation.tipodespesa.model.TipoDespesaUi
import br.com.edificiopromenade.presentation.util.MoneyFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DespesaViewModel @Inject constructor(
    private val cadastrarDespesaUseCase: CadastrarDespesaUseCase,
    private val consultarDespesasPorFechamentoUseCase: ConsultarDespesasPorFechamentoUseCase,
    private val excluirDespesaUseCase: ExcluirDespesaUseCase,
    private val excluirParcelaDespesaAvulsaUseCase: ExcluirParcelaDespesaAvulsaUseCase,
    private val consultarTiposDespesaUiUseCase: ConsultarTiposDespesaUiUseCase,
    private val finalizarFechamentoUseCase: FinalizarFechamentoUseCase,
    private val verificarDespesaExistenteUseCase: VerificarDespesaExistenteUseCase,
    private val fechamentoRepository: FechamentoRepository,
    private val despesaRepository: DespesaRepository,
    private val despesaTemporariaParcelaRepository: DespesaTemporariaParcelaRepository,
    private val despesaTemporariaRepository: DespesaTemporariaRepository,
    private val verificarNomeReservadoTipoDespesaUseCase: VerificarNomeReservadoTipoDespesaUseCase,
    private val atualizarDespesaUseCase: AtualizarDespesaUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            DespesaUiState()
        )

    val uiState = _uiState.asStateFlow()

    private var fechamentoId: Long = 0

    fun carregar(
        fechamentoId: Long
    ) {
        this.fechamentoId = fechamentoId

        viewModelScope.launch {
            val fechamento = fechamentoRepository.findById(
                    fechamentoId
                )
                ?: return@launch

            val fechamentoAnterior =
                fechamentoRepository.findAnterior(
                    mes = fechamento.mes,
                    ano = fechamento.ano
                )

            val fechamentoProximo =
                fechamentoRepository.findProximo(
                    mes = fechamento.mes,
                    ano = fechamento.ano
                )

            _uiState.value =
                _uiState.value.copy(

                    fechamentoAtualId = fechamento.id,
                    fechamentoFinalizado = fechamento.finalizado,

                    competencia = "${fechamento.mes.toString().padStart(
                                2, '0')}/${fechamento.ano}",

                    fechamentoAnteriorId = fechamentoAnterior?.id,
                    fechamentoProximoId = fechamentoProximo?.id
                )
        }

        viewModelScope.launch {
            consultarDespesasPorFechamentoUseCase(
                fechamentoId
            ).collect { lista ->

                val despesasComItens = despesaRepository
                        .findComItensListByFechamento(
                            fechamentoId
                        )

                val idsComItens = despesasComItens
                        .filter { it.itens.isNotEmpty()}
                        .map {it.despesa.id}
                        .toSet()

                _uiState.value =
                    _uiState.value.copy(

                        despesas = lista.map { despesaComTipo ->
                                despesaComTipo
                                    .toUi()
                                    .copy(
                                        possuiItens = despesaComTipo.despesa.id in idsComItens
                                    )
                            }
                    )
            }
        }

        viewModelScope.launch {
            consultarTiposDespesaUiUseCase()
                .collect { listaUi ->

                    _uiState.value =
                        _uiState.value.copy(
                            tiposDespesa = listaUi,
                            tipoSelecionado = _uiState.value.tipoSelecionado
                                    ?: listaUi.firstOrNull()
                        )
                }
        }
    }

    fun editarDespesa(
        despesa: DespesaUi
    ) {

        if (_uiState.value.fechamentoFinalizado) {
            return
        }

        viewModelScope.launch {

            val despesaComItens = despesaRepository.findComItens(
                    despesa.id
                )

            if (
                despesaComItens != null &&
                despesaComItens.itens.isNotEmpty()
            ) {

                _uiState.value =
                    _uiState.value.copy(
                        mensagem = UiMessage.Error(
                            "Esta despesa possui itens. Altere os valores pela tela Itens."
                        )
                    )
                return@launch
            }

            var descricaoParaEdicao = ""

            if (despesa.avulsa) {

                val parcela =
                    despesaTemporariaParcelaRepository
                        .findByDespesaId(
                            despesa.id
                        )

                if (parcela != null) {

                    val despesaTemporaria =
                        despesaTemporariaRepository.findById(
                            parcela.despesaTemporariaId
                        )

                    descricaoParaEdicao =
                        despesaTemporaria?.descricao
                            ?: despesa.descricao
                }
            }

            _uiState.value =
                _uiState.value.copy(
                    despesaEmEdicao = despesa,
                    descricaoLivre = descricaoParaEdicao,
                    valor = MoneyFormatter.format(
                            (despesa.valor * 100)
                                .toLong()
                                .toString()
                        ),
                    mensagem = null,
                    expandirTipos = false
                )
        }
    }

    fun cancelarEdicao() {

        _uiState.value =
            _uiState.value.copy(
                despesaEmEdicao = null,
                valor = "",
                descricaoLivre = "",
                mensagem = null
            )
    }

    fun abrirFechamentoAnterior(
        onAbrir: (Long) -> Unit
    ) {
        val id = _uiState.value.fechamentoAnteriorId
                ?: return
        onAbrir(id)
    }

    fun abrirFechamentoProximo(
        onAbrir: (Long) -> Unit
    ) {
        val id = _uiState.value.fechamentoProximoId
                ?: return
        onAbrir(id)
    }

    fun onValorChanged(
        valor: String
    ) {
        val cleanValor = valor.filter { it.isDigit() }
        if (cleanValor.length > 12) return

        _uiState.value =
            _uiState.value.copy(
                valor = MoneyFormatter.format(
                    cleanValor
                )
            )
    }

    fun onDescricaoChanged(
        descricao: String
    ) {

        val despesaEmEdicao = _uiState.value.despesaEmEdicao

        /*
         * Se estivermos editando uma despesa fixa,
         * nenhuma alteração de nome é aceita.
         */
        if (
            despesaEmEdicao != null &&
            !despesaEmEdicao.avulsa
        ) {
            return
        }

        _uiState.value =
            _uiState.value.copy(
                descricaoLivre = descricao
            )
    }

    fun salvar() {
        viewModelScope.launch {

            val valorDespesa = stateToDouble()

            if (valorDespesa == null || valorDespesa <= 0.0) {
                _uiState.value =
                    _uiState.value.copy(
                        mensagem = UiMessage.Error(
                            "Informe um valor válido para a despesa."
                        )
                    )
                return@launch
            }

//            val despesaEmEdicao = _uiState.value.despesaEmEdicao
//
//            if (despesaEmEdicao != null) {

                val despesaEmEdicao = _uiState.value.despesaEmEdicao

                if (despesaEmEdicao != null) {

                    /*
                     * =====================================================
                     * EDIÇÃO DE DESPESA EXISTENTE
                     * =====================================================
                     *
                     * FIXA:
                     * - nome nunca é alterado;
                     * - somente valor pode mudar.
                     *
                     * AVULSA:
                     * - nome pode mudar;
                     * - valor pode mudar.
                     */

                    if (
                        despesaEmEdicao.avulsa &&
                        _uiState.value.descricaoLivre.isBlank()
                    ) {

                        _uiState.value =
                            _uiState.value.copy(
                                mensagem = UiMessage.Error(
                                    "Informe o nome da despesa avulsa."
                                )
                            )

                        return@launch
                    }

                    /*
                     * A verificação por nome só faz sentido
                     * para uma despesa avulsa.
                     */
                    if (despesaEmEdicao.avulsa) {

                        val novoNome =
                            _uiState.value
                                .descricaoLivre
                                .trim()

                        /*
                         * PRIMEIRA VERIFICAÇÃO:
                         *
                         * O nome pertence ao catálogo
                         * de despesas fixas?
                         */
                        if (
                            verificarNomeReservadoTipoDespesaUseCase(
                                novoNome
                            )
                        ) {

                            _uiState.value =
                                _uiState.value.copy(
                                    mensagem = UiMessage.Error(
                                        "O nome '$novoNome' pertence a uma despesa fixa " +
                                                "e não pode ser usado em uma despesa avulsa."
                                    )
                                )

                            return@launch
                        }

                        /*
                         * SEGUNDA VERIFICAÇÃO:
                         *
                         * O nome já é utilizado por outra
                         * despesa deste fechamento?
                         */
                        if (
                            existeNomeDespesaNoFechamento(
                                novoNome = novoNome,
                                despesaIdIgnorar = despesaEmEdicao.id
                            )
                        ) {

                            _uiState.value =
                                _uiState.value.copy(
                                    mensagem = UiMessage.Error(
                                        "Já existe uma despesa com o nome " +
                                                "'$novoNome' neste fechamento."
                                    )
                                )

                            return@launch
                        }
                    }

                    try {

                        atualizarDespesaUseCase(
                            AtualizarDespesaCommand(

                                despesaId =
                                    despesaEmEdicao.id,

                                descricao =
                                    if (
                                        despesaEmEdicao.avulsa
                                    ) {

                                        _uiState.value
                                            .descricaoLivre
                                            .trim()

                                    } else {

                                        ""
                                    },

                                valor =
                                    valorDespesa
                            )
                        )

                        _uiState.value =
                            _uiState.value.copy(

                                despesaEmEdicao = null,

                                valor = "",

                                descricaoLivre = "",

                                mensagem =
                                    UiMessage.Success(
                                        "Despesa atualizada com sucesso."
                                    )
                            )

                    } catch (
                        exception: IllegalStateException
                    ) {

                        _uiState.value =
                            _uiState.value.copy(

                                mensagem =
                                    UiMessage.Error(
                                        exception.message
                                            ?: "Não foi possível atualizar a despesa."
                                    )
                            )

                    } catch (
                        exception: Exception
                    ) {

                        _uiState.value =
                            _uiState.value.copy(

                                mensagem =
                                    UiMessage.Error(
                                        "Não foi possível atualizar a despesa."
                                    )
                            )
                    }

                    /*
                     * FUNDAMENTAL:
                     *
                     * impede que o fluxo continue para
                     * o bloco de cadastro de nova despesa.
                     */
                    return@launch

////                    val entidade =
////                        despesaRepository.findById(
////                            despesaEmEdicao.id
////                        )
////                            ?: return@launch
//
//                    /*
//                     * Para despesa fixa deixamos vazio.
//                     *
//                     * O nome da despesa fixa deve vir SEMPRE
//                     * de TipoDespesaEntity.
//                     *
//                     * Isso também corrige despesas fixas que
//                     * eventualmente tenham recebido um
//                     * descricaoLivre indevidamente.
//                     */
//                    var novaDescricaoLivre =
//                        if (despesaEmEdicao.avulsa) {
//                            entidade.descricaoLivre
//                        } else {
//                            ""
//                        }
//
//                    /*
//                     * =====================================================
//                     * SOMENTE DESPESA AVULSA
//                     * =====================================================
//                     */
//                    if (despesaEmEdicao.avulsa) {
//
//                        val parcela =
//                            despesaTemporariaParcelaRepository
//                                .findByDespesaId(
//                                    entidade.id
//                                )
//
//                        if (parcela == null) {
//
//                            _uiState.value =
//                                _uiState.value.copy(
//                                    mensagem = UiMessage.Error(
//                                        "Não foi possível localizar a parcela da despesa avulsa."
//                                    )
//                                )
//
//                            return@launch
//                        }
//
//                        val despesaTemporaria =
//                            despesaTemporariaRepository.findById(
//                                parcela.despesaTemporariaId
//                            )
//
//                        if (despesaTemporaria == null) {
//
//                            _uiState.value =
//                                _uiState.value.copy(
//                                    mensagem = UiMessage.Error(
//                                        "Não foi possível localizar a despesa avulsa original."
//                                    )
//                                )
//
//                            return@launch
//                        }
//
//                        val novoNome =
//                            _uiState.value
//                                .descricaoLivre
//                                .trim()
//
//                        /*
//                         * Atualiza o nome original da
//                         * despesa temporária.
//                         */
//                        despesaTemporariaRepository.update(
//                            despesaTemporaria.copy(
//                                descricao = novoNome
//                            )
//                        )
//
//                        /*
//                         * Calcula o nome da parcela que aparece
//                         * na lista de despesas.
//                         */
//                        novaDescricaoLivre =
//                            if (
//                                despesaTemporaria.quantidadeParcelas > 1
//                            ) {
//
//                                "$novoNome - Parcela " +
//                                        "${parcela.numeroParcela}/" +
//                                        "${despesaTemporaria.quantidadeParcelas}"
//
//                            } else {
//
//                                novoNome
//                            }
//                    }
//
//                    /*
//                     * Atualiza a DespesaEntity.
//                     *
//                     * FIXA:
//                     * descricaoLivre = ""
//                     *
//                     * AVULSA:
//                     * descricaoLivre = novo nome
//                     */
//                    despesaRepository.update(
//                        entidade.copy(
//                            descricaoLivre = novaDescricaoLivre,
//                            valor = valorDespesa
//                        )
//                    )
//
//                    /*
//                     * Somente despesas avulsas possuem registro
//                     * em despesas_temporarias_parcelas.
//                     */
//                    if (despesaEmEdicao.avulsa) {
//
//                        despesaTemporariaParcelaRepository
//                            .atualizarValorPorDespesaId(
//                                despesaId = entidade.id,
//                                valor = valorDespesa
//                            )
//                    }
//
//                    /*
//                     * Sai do modo de edição.
//                     */
//                    _uiState.value =
//                        _uiState.value.copy(
//                            despesaEmEdicao = null,
//                            valor = "",
//                            descricaoLivre = "",
//                            mensagem = UiMessage.Success(
//                                "Despesa atualizada com sucesso."
//                            )
//                        )

//                    /*
//                     * FUNDAMENTAL:
//                     *
//                     * impede que o fluxo continue para
//                     * o bloco de cadastro de nova despesa.
//                     */
//                    return@launch
                }
//            }

            val tipoSelecionado = _uiState.value.tipoSelecionado

            if (tipoSelecionado == null) {

                _uiState.value =
                    _uiState.value.copy(
                        mensagem = UiMessage.Error(
                            "Selecione o tipo de despesa."
                        )
                    )

                return@launch
            }

            if (
                verificarDespesaExistenteUseCase(
                    fechamentoId,
                    tipoSelecionado.id
                )
            ) {

                _uiState.value =
                    _uiState.value.copy(
                        mensagem = UiMessage.Error(
                            "A despesa '${tipoSelecionado.descricao}' já foi cadastrada."
                        )
                    )

                return@launch
            }

            cadastrarDespesaUseCase(
                CadastrarDespesaCommand(
                    fechamentoId = fechamentoId,
                    tipoDespesaId = _uiState.value.tipoSelecionado!!.id,
                    descricaoLivre = _uiState.value.descricaoLivre,
                    valor = valorDespesa
                )
            )

            _uiState.value =
                _uiState.value.copy(
                    valor = "",
                    descricaoLivre = ""
                )

            _uiState.value =
                _uiState.value.copy(
                    valor = "",
                    mensagem = UiMessage.Success(
                     "Despesa cadastrada com sucesso."
                    )
                )
            return@launch
        }
    }

    private suspend fun existeNomeDespesaNoFechamento(
        novoNome: String,
        despesaIdIgnorar: Long
    ): Boolean {

        val nomeNovoNormalizado =
            normalizarNomeDespesa(
                novoNome
            )

        /*
         * Percorre todas as despesas já carregadas
         * para o fechamento atual.
         */
        for (
            outraDespesa in
            _uiState.value.despesas
        ) {

            /*
             * A própria despesa que está sendo editada
             * não deve ser comparada consigo mesma.
             */
            if (
                outraDespesa.id == despesaIdIgnorar
            ) {
                continue
            }

            var nomeExistente = outraDespesa.descricao

            /*
             * Para uma despesa avulsa parcelada,
             * outraDespesa.descricao pode estar assim:
             *
             * Pintura - Parcela 1/3
             *
             * O nome que queremos comparar é o nome
             * original da DespesaTemporaria:
             *
             * Pintura
             */
            if (outraDespesa.avulsa) {

                val parcela =
                    despesaTemporariaParcelaRepository
                        .findByDespesaId(
                            outraDespesa.id
                        )

                if (parcela != null) {

                    val despesaTemporaria = despesaTemporariaRepository
                            .findById(
                                parcela.despesaTemporariaId
                            )

                    if (despesaTemporaria != null) {

                        nomeExistente = despesaTemporaria.descricao
                    }
                }
            }

            if (
                normalizarNomeDespesa(
                    nomeExistente
                ) == nomeNovoNormalizado
            ) {
                return true
            }
        }
        return false
    }

    private fun normalizarNomeDespesa(
        nome: String
    ): String {

        return nome
            .trim()
            .replace(
                Regex("\\s+"),
                " "
            )
            .lowercase()
    }

    private fun stateToDouble(): Double? {
        return _uiState.value.valor
            .replace(".", "")
            .replace(",", ".")
            .toDoubleOrNull()
    }

    fun solicitarExclusao(
        despesa: DespesaUi
    ) {
        _uiState.value =
            _uiState.value.copy(
                despesaSelecionadaParaExclusao = despesa
            )
    }

    fun cancelarExclusao() {
        _uiState.value =
            _uiState.value.copy(
                despesaSelecionadaParaExclusao = null
            )
    }

    /*
     * ============================================================
     * EXCLUI DESPESA COMPLETA
     * ============================================================
     *
     * FIXA:
     * remove somente aquela DespesaEntity.
     *
     * AVULSA:
     * remove toda a estrutura aberta/futura da
     * despesa avulsa.
     */
    fun confirmarExclusaoDespesa() {

        val despesa =
            _uiState.value
                .despesaSelecionadaParaExclusao
                ?: return

        viewModelScope.launch {

            try {
                excluirDespesaUseCase(
                    ExcluirDespesaCommand(
                        despesa.id
                    )
                )

                _uiState.value =
                    _uiState.value.copy(
                        despesaSelecionadaParaExclusao = null,
                        despesaEmEdicao = null,
                        valor = "",
                        descricaoLivre = "",
                        mensagem = UiMessage.Success(
                                if (despesa.avulsa) {
                                    "Despesa avulsa excluída com sucesso."
                                } else {
                                    "Despesa excluída com sucesso."
                                }
                            )
                    )
            } catch (
                exception: IllegalStateException
            ) {

                _uiState.value =
                    _uiState.value.copy(
                        despesaSelecionadaParaExclusao = null,
                        mensagem = UiMessage.Error(
                                exception.message
                                    ?: "Não foi possível excluir a despesa."
                            )
                    )
            } catch (
                exception: Exception
            ) {

                _uiState.value =
                    _uiState.value.copy(
                        despesaSelecionadaParaExclusao = null,
                        mensagem = UiMessage.Error(
                                "Não foi possível excluir a despesa."
                            )
                    )
            }
        }
    }

    /*
     * ============================================================
     * EXCLUI SOMENTE UMA PARCELA DA DESPESA AVULSA
     * ============================================================
     */
    fun confirmarExclusaoParcelaAvulsa() {

        val despesa =
            _uiState.value
                .despesaSelecionadaParaExclusao
                ?: return

        /*
         * Proteção adicional da camada de apresentação.
         *
         * O Repository também protege a operação,
         * mas não devemos chamar este fluxo para
         * uma despesa fixa.
         */
        if (!despesa.avulsa) {

            _uiState.value =
                _uiState.value.copy(
                    despesaSelecionadaParaExclusao = null,
                    mensagem = UiMessage.Error(
                            "Esta operação é permitida somente para despesas avulsas."
                        )
                )
            return
        }

        viewModelScope.launch {

            try {
                excluirParcelaDespesaAvulsaUseCase(
                    despesa.id
                )

                _uiState.value =
                    _uiState.value.copy(
                        despesaSelecionadaParaExclusao = null,
                        despesaEmEdicao = null,
                        valor = "",
                        descricaoLivre = "",
                        mensagem = UiMessage.Success(
                                "Parcela excluída com sucesso."
                            )
                    )

            } catch (
                exception: IllegalStateException
            ) {

                _uiState.value =
                    _uiState.value.copy(
                        despesaSelecionadaParaExclusao = null,
                        mensagem = UiMessage.Error(
                                exception.message
                                    ?: "Não foi possível excluir a parcela."
                            )
                    )

            } catch (
                exception: Exception
            ) {

                _uiState.value =
                    _uiState.value.copy(
                        despesaSelecionadaParaExclusao = null,
                        mensagem = UiMessage.Error(
                                "Não foi possível excluir a parcela."
                            )
                    )
            }
        }
    }

    fun limparMensagem() {
        _uiState.value =
            _uiState.value.copy(
                mensagem = null,
                valor = "",
                descricaoLivre = ""
            )
    }

    fun alterarExpandido(
        expandido: Boolean
    ) {
        if (_uiState.value.despesaEmEdicao != null) {
            return
        }
        _uiState.value =
            _uiState.value.copy(
                expandirTipos = expandido
            )
    }

    fun selecionarTipo(
        tipo: TipoDespesaUi
    ) {
        _uiState.value =
            _uiState.value.copy(
                tipoSelecionado = tipo,
                expandirTipos = false
            )
    }

    fun finalizarFechamento(
        onSucesso: () -> Unit
    ) {
        viewModelScope.launch {

            /*
             * Validação rápida da apresentação.
             *
             * O UseCase também valida isso.
             * Mantemos aqui para dar retorno imediato
             * ao usuário.
             */
            if (_uiState.value.despesas.isEmpty()) {

                _uiState.value =
                    _uiState.value.copy(
                        mensagem = UiMessage.Error(
                            "Cadastre pelo menos uma despesa antes de finalizar o fechamento."
                        )
                    )
                return@launch
            }

            try {

                /*
                 * A partir daqui toda a operação é atômica:
                 *
                 * - valida fechamento;
                 * - calcula rateio;
                 * - exclui demonstrativos anteriores;
                 * - grava novos demonstrativos;
                 * - marca fechamento como finalizado.
                 *
                 * Se qualquer etapa falhar, ocorre ROLLBACK.
                 */
                finalizarFechamentoUseCase(
                    fechamentoId
                )

                /*
                 * Só navegamos para os demonstrativos
                 * depois que o UseCase terminou sem erro.
                 *
                 * Portanto a transação já foi concluída.
                 */
                onSucesso()

            } catch (
                exception: IllegalStateException
            ) {

                _uiState.value =
                    _uiState.value.copy(
                        mensagem = UiMessage.Error(
                            exception.message
                                ?: "Não foi possível finalizar o fechamento."
                        )
                    )

            } catch (
                exception: Exception
            ) {

                _uiState.value =
                    _uiState.value.copy(
                        mensagem = UiMessage.Error(
                            "Não foi possível finalizar o fechamento."
                        )
                    )
            }
        }
    }

}