package br.com.edificiopromenade.presentation.despesaitem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.edificiopromenade.data.local.entity.DespesaItemEntity
import br.com.edificiopromenade.domain.usecase.despesa.AtualizarValorDespesaUseCase
import br.com.edificiopromenade.domain.usecase.despesaitem.CadastrarItemDespesaUseCase
import br.com.edificiopromenade.domain.usecase.despesaitem.CalcularTotalItensDespesaUseCase
import br.com.edificiopromenade.domain.usecase.despesaitem.ConsultarItensDespesaUiUseCase
import br.com.edificiopromenade.domain.usecase.despesaitem.ExcluirItemDespesaUseCase
import br.com.edificiopromenade.presentation.despesaitem.mapper.toEntity
import br.com.edificiopromenade.presentation.common.message.UiMessage
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
    private val cadastrarItemDespesaUseCase: CadastrarItemDespesaUseCase,
    private val calcularTotalItensUseCase: CalcularTotalItensDespesaUseCase,
    private val atualizarValorDespesaUseCase: AtualizarValorDespesaUseCase,
    private val excluirItemDespesaUseCase: ExcluirItemDespesaUseCase
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

            val valor = stateToDouble()

            if (valor == null || valor <= 0.0) {
                _uiState.value =
                    _uiState.value.copy(
                        mensagem = UiMessage.Error(
                            "Informe um valor válido."
                            )
                    )
                return@launch
            }

            cadastrarItemDespesaUseCase(
                DespesaItemEntity(
                    despesaId = despesaId,
                    descricao = _uiState.value.descricao,
                    valor = valor
                )
            )

            atualizarTotalDespesa()

            _uiState.value =
                _uiState.value.copy(
                    descricao = "",
                    valor = "",
                    mensagem = UiMessage.Success(
                            "Item cadastrado."
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

    fun confirmarExclusao() {
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
                            "Item excluído."
                        )
                )
        }
    }

    private suspend fun atualizarTotalDespesa() {
        val total = calcularTotalItensUseCase(
            despesaId
        )
        atualizarValorDespesaUseCase(
            despesaId,
            total
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