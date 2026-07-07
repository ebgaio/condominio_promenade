package br.com.edificiopromenade.presentation.despesaitem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.edificiopromenade.domain.usecase.despesa.AtualizarTotalDespesaUseCase
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
    private val atualizarTotalDespesaUseCase: AtualizarTotalDespesaUseCase,
    private val excluirItemDespesaUseCase: ExcluirItemDespesaUseCase,
    private val atualizarDespesaItemUseCase: AtualizarDespesaItemUseCase
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
        viewModelScope.launch {

            val valorItem = stateToDouble()

            if (valorItem == null || valorItem <= 0.0) {
                _uiState.value =
                    _uiState.value.copy(
                        mensagem = UiMessage.Error(
                            "Informe um valor válido."
                            )
                    )
                return@launch
            }

            val itemEdicao = uiState.value.itemEmEdicao

            if (itemEdicao == null) {
                cadastrarDespesaItemUseCase(
                    DespesaItemUi(
                        id = 0,
                        despesaId = despesaId,
                        descricao = uiState.value.descricao,
                        valor = valorItem
                    ).toEntity()
                )
            } else {
                atualizarDespesaItemUseCase(
                    itemEdicao.copy(
                        descricao = uiState.value.descricao,
                        valor = valorItem
                    ).toEntity()
                )
            }

            atualizarTotalDespesa()

            _uiState.value =
                _uiState.value.copy(
                    descricao = "",
                    valor = "",
                    itemEmEdicao = null,
                    mensagem = UiMessage.Success(
                        if (itemEdicao == null)
                            "Item cadastrado com sucesso."
                        else
                            "Item atualizado com sucesso."
                        )
                )
        }
    }

    private fun stateToDouble(): Double? {
        return _uiState.value.valor
            .replace(".", "")
            .replace(",", ".")
            .toDoubleOrNull()
    }

    fun solicitarExclusao(
        item: DespesaItemUi
    ) {
        _uiState.value =
            _uiState.value.copy(
                itemSelecionadoParaExclusao = item
            )
    }

    fun excluir() {
        val item = _uiState.value.itemSelecionadoParaExclusao
                ?: return

        viewModelScope.launch {
            excluirItemDespesaUseCase(
                item.toEntity()
            )

            atualizarTotalDespesa()

            _uiState.value =
                _uiState.value.copy(
                    itemSelecionadoParaExclusao = null,
                    mensagem = UiMessage.Success(
                            "Item excluído com sucesso."
                        )
                )
        }
    }

    fun editar(
        item: DespesaItemUi
    ) {
        _uiState.value =
            _uiState.value.copy(
                itemEmEdicao = item,
                descricao = item.descricao,
                valor = MoneyFormatter.format(
                    (item.valor * 100).toLong().toString()
                )
            )
    }

    private suspend fun atualizarTotalDespesa() {
        atualizarTotalDespesaUseCase(
            despesaId
        )
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
}