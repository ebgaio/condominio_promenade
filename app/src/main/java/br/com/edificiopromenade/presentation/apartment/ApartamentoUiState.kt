package br.com.edificiopromenade.presentation.apartment

data class ApartamentoUiState(

    val id: Long = 0,

    val numero: String = "",

    val fracaoIdealAtual: String = "",

    val ativo: Boolean = true,

    val salvoComSucesso: Boolean = false
)