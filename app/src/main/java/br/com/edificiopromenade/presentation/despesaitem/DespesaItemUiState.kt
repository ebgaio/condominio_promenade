package br.com.edificiopromenade.presentation.despesaitem

import br.com.edificiopromenade.presentation.common.model.DespesaUi

data class DespesaItemUiState(

    val itens: List<DespesaUi> = emptyList()
)