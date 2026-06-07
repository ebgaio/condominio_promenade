package br.com.edificiopromenade.presentation.moradores

import br.com.edificiopromenade.data.local.relation.ApartamentoComMorador

data class MoradoresUiState(

    val isLoading: Boolean = false,

    val apartamentos: List<ApartamentoComMorador> = emptyList(),

    val error: String? = null,

    val moradorId: Long = 0,

    val apartamentoIdSelecionado: Long = 0,

    val nome: String = "",

    val salvoComSucesso: Boolean = false,

    val mensagem: String? = null
)