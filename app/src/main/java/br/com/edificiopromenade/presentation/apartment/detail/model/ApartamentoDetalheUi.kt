package br.com.edificiopromenade.presentation.apartment.detail.model

import br.com.edificiopromenade.presentation.resident.model.MoradorUi

data class ApartamentoDetalheUi(

    val id: Long = 0,

    val condominioId: Long = 0,

    val numero: String = "",

    val percentualCopasa: Double = 0.0,

    val ativo: Boolean = true,

    val moradores: List<MoradorUi> = emptyList()
)