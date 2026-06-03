package br.com.edificiopromenade.presentation.moradores

import br.com.edificiopromenade.data.local.relation.ApartamentoComMorador

data class MoradoresUiState(

    val isLoading: Boolean = false,

    val apartamentos: List<ApartamentoComMorador> = emptyList(),

    val error: String? = null
)