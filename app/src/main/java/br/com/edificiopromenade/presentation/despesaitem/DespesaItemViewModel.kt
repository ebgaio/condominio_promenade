package br.com.edificiopromenade.presentation.despesaitem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.edificiopromenade.domain.repository.DespesaRepository
import br.com.edificiopromenade.domain.repository.FechamentoRepository
import br.com.edificiopromenade.domain.repository.TipoDespesaRepository
import br.com.edificiopromenade.domain.usecase.despesaitem.AtualizarDespesaItemUseCase
import br.com.edificiopromenade.domain.usecase.despesaitem.CadastrarDespesaItemUseCase
import br.com.edificiopromenade.domain.usecase.despesaitem.ConsultarItensDespesaUiUseCase
import br.com.edificiopromenade.domain.usecase.despesaitem.ExcluirItemDespesaUseCase
import br.com.edificiopromenade.presentation.common.message.UiMessage
import br.com.edificiopromenade.presentation.despesaitem.mapper.toEntity
import br.com.edificiopromenade.presentation.despesaitem.model.DespesaItemUi
import br.com.edificiopromenade.presentation.util.MoneyFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DespesaItemViewModel @Inject constructor(
    private val consultarItensDespesaUiUseCase: ConsultarItensDespesaUiUseCase,
    private val cadastrarDespesaItemUseCase: CadastrarDespesaItemUseCase,
    private val excluirItemDespesaUseCase: ExcluirItemDespesaUseCase,
    private val atualizarDespesaItemUseCase: AtualizarDespesaItemUseCase,
    private val despesaRepository: DespesaRepository,
    private val fechamentoRepository: FechamentoRepository,
    private val tipoDespesaRepository: TipoDespesaRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            DespesaItemUiState()
        )

    val uiState = _uiState.asStateFlow()

    private var despesaId: Long = 0

    fun carregar(
        despesaId: Long
    ) {
        this.despesaId = despesaId

        /*
         * Localiza a despesa e, através dela,
         * descobre o fechamento ao qual pertence.
         *
         * Aqui carregamos:
         *
         * - situação do fechamento;
         * - competência.
         */
        viewModelScope.launch {
            val despesa =
                despesaRepository.findById(
                    despesaId
                )
                    ?: return@launch

            val fechamento =
                fechamentoRepository.findById(
                    despesa.fechamentoId
                )
                    ?: return@launch

            val tipoDespesa =
                tipoDespesaRepository.findById(
                    despesa.tipoDespesaId
                )


            /*
             * A tela de itens também deve respeitar
             * o snapshot histórico.
             */
            val avulsa =
                tipoDespesa
                    ?.descricao
                    ?.equals(
                        "Despesa Avulsa",
                        ignoreCase = true
                    ) == true

            val nomeAtual =
                if (avulsa) {

                    despesa.descricaoLivre
                        .ifBlank {
                            tipoDespesa?.descricao
                                ?: "Despesa"
                        }

                } else {

                    tipoDespesa?.descricao
                        ?: despesa.descricaoLivre
                            .ifBlank {
                                "Despesa"
                            }
                }

            val nomeDespesa =
                despesa.descricaoHistorica
                    ?: nomeAtual

            _uiState.value =
                _uiState.value.copy(

                    fechamentoFinalizado =
                        fechamento.finalizado,

                    competencia =
                        "${
                            fechamento.mes
                                .toString()
                                .padStart(2, '0')
                        }/${fechamento.ano}",

                    nomeDespesa =
                        nomeDespesa
                )
        }

        /*
         * =====================================================
         * CARREGA OS ITENS
         * =====================================================
         */
        viewModelScope.launch {

            consultarItensDespesaUiUseCase(
                despesaId
            ).collect { lista ->

                _uiState.value =
                    _uiState.value.copy(
                        itens = lista
                    )
            }
        }
    }

    fun salvar() {

        /*
         * Primeira barreira:
         * proteção da própria ViewModel.
         */
        if (_uiState.value.fechamentoFinalizado) {

            informarFechamentoFinalizado()

            return
        }

        viewModelScope.launch {

            val valorItem =
                stateToDouble()

            if (
                valorItem == null ||
                valorItem <= 0.0
            ) {

                _uiState.value =
                    _uiState.value.copy(
                        mensagem =
                            UiMessage.Error(
                                "Informe um valor válido."
                            )
                    )

                return@launch
            }

            val itemEdicao =
                _uiState.value.itemEmEdicao

            try {

                /*
                 * =================================================
                 * NOVO ITEM
                 * =================================================
                 */
                if (itemEdicao == null) {

                    cadastrarDespesaItemUseCase(
                        DespesaItemUi(
                            id = 0,
                            despesaId = despesaId,
                            descricao =
                                _uiState.value.descricao,
                            valor = valorItem
                        ).toEntity()
                    )

                } else {

                    /*
                     * =============================================
                     * ALTERAÇÃO
                     * =============================================
                     */
                    atualizarDespesaItemUseCase(
                        itemEdicao.copy(
                            descricao =
                                _uiState.value.descricao,
                            valor = valorItem
                        ).toEntity()
                    )
                }

                /*
                 * NÃO recalculamos o total aqui.
                 *
                 * Os próprios use cases já executam:
                 *
                 * AtualizarTotalDespesaUseCase
                 */

                _uiState.value =
                    _uiState.value.copy(

                        descricao = "",

                        valor = "",

                        itemEmEdicao = null,

                        mensagem =
                            UiMessage.Success(
                                if (itemEdicao == null) {

                                    "Item cadastrado com sucesso."

                                } else {

                                    "Item atualizado com sucesso."
                                }
                            )
                    )

            } catch (
                exception: IllegalStateException
            ) {

                /*
                 * Segunda barreira:
                 *
                 * O UseCase pode detectar que o fechamento
                 * foi finalizado depois que a tela foi
                 * carregada.
                 */
                _uiState.value =
                    _uiState.value.copy(
                        mensagem =
                            UiMessage.Error(
                                exception.message
                                    ?: "Não foi possível alterar o item."
                            )
                    )
            }
        }
    }

    private fun stateToDouble():
            Double? {

        return _uiState.value.valor
            .replace(".", "")
            .replace(",", ".")
            .toDoubleOrNull()
    }

    fun solicitarExclusao(
        item: DespesaItemUi
    ) {

        if (_uiState.value.fechamentoFinalizado) {

            informarFechamentoFinalizado()

            return
        }

        _uiState.value =
            _uiState.value.copy(
                itemSelecionadoParaExclusao =
                    item
            )
    }

    fun excluir() {

        if (_uiState.value.fechamentoFinalizado) {

            informarFechamentoFinalizado()

            return
        }

        val item =
            _uiState.value
                .itemSelecionadoParaExclusao
                ?: return

        viewModelScope.launch {

            try {

                excluirItemDespesaUseCase(
                    item.toEntity()
                )

                /*
                 * O próprio use case já atualiza
                 * o total da despesa.
                 */

                _uiState.value =
                    _uiState.value.copy(

                        itemSelecionadoParaExclusao =
                            null,

                        mensagem =
                            UiMessage.Success(
                                "Item excluído com sucesso."
                            )
                    )

            } catch (
                exception: IllegalStateException
            ) {

                _uiState.value =
                    _uiState.value.copy(

                        itemSelecionadoParaExclusao =
                            null,

                        mensagem =
                            UiMessage.Error(
                                exception.message
                                    ?: "Não foi possível excluir o item."
                            )
                    )
            }
        }
    }

    fun editar(
        item: DespesaItemUi
    ) {

        if (_uiState.value.fechamentoFinalizado) {

            informarFechamentoFinalizado()

            return
        }

        _uiState.value =
            _uiState.value.copy(

                itemEmEdicao =
                    item,

                descricao =
                    item.descricao,

                valor =
                    MoneyFormatter.format(
                        (item.valor * 100)
                            .toLong()
                            .toString()
                    )
            )
    }

    fun onValorChanged(
        valor: String
    ) {

        if (_uiState.value.fechamentoFinalizado) {
            return
        }

        val cleanValor =
            valor.filter {
                it.isDigit()
            }

        if (cleanValor.length > 12) {
            return
        }

        _uiState.value =
            _uiState.value.copy(
                valor =
                    MoneyFormatter.format(
                        cleanValor
                    )
            )
    }

    fun onDescricaoChanged(
        descricao: String
    ) {

        if (_uiState.value.fechamentoFinalizado) {
            return
        }

        _uiState.value =
            _uiState.value.copy(
                descricao = descricao
            )
    }

    fun limparMensagem() {

        _uiState.value =
            _uiState.value.copy(
                mensagem = null,
                valor = "",
                descricao = ""
            )
    }

    /*
     * =========================================================
     * MENSAGEM ÚNICA PARA PROTEÇÃO DO HISTÓRICO
     * =========================================================
     */
    private fun informarFechamentoFinalizado() {

        _uiState.value =
            _uiState.value.copy(
                mensagem =
                    UiMessage.Error(
                        "Este fechamento já foi finalizado " +
                                "e não pode ser alterado."
                    )
            )
    }
}