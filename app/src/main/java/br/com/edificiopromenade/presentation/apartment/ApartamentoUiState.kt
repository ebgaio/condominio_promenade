package br.com.edificiopromenade.presentation.apartment

import br.com.edificiopromenade.data.local.entity.ApartamentoEntity

data class ApartamentoUiState(

    val id: Long = 0,

    val numero: String = "",

    val fracaoIdealAtual: String = "",

    val ativo: Boolean = true,

    val salvoComSucesso: Boolean = false,

    val apartamentos: List<ApartamentoEntity> = emptyList()
)