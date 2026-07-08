package br.com.edificiopromenade.presentation.apartment.model

data class ApartamentoUi(

    val id: Long = 0,

    val condominioId: Long = 0,

    val numero: String = "",

    val percentualCopasa: Double = 0.0,

    val ativo: Boolean = true
)