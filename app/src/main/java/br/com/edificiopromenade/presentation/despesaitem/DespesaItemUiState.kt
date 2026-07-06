package br.com.edificiopromenade.presentation.despesaitem

import br.com.edificiopromenade.presentation.common.message.UiMessage
import br.com.edificiopromenade.presentation.despesaitem.model.DespesaItemUi

data class DespesaItemUiState(

    val descricao: String = "",

    val valor: String = "",

    val itens: List<DespesaItemUi> = emptyList(),

    val mensagem: UiMessage? = null,

    val itemSelecionadoParaExclusao: DespesaItemUi? = null
)