package br.com.edificiopromenade.presentation.despesaitem

import br.com.edificiopromenade.presentation.common.message.UiMessage
import br.com.edificiopromenade.presentation.common.model.DespesaUi

data class DespesaItemUiState(

    val descricao: String = "",

    val valor: String = "",

    val itens: List<DespesaUi> = emptyList(),

    val mensagem: UiMessage? = null
)