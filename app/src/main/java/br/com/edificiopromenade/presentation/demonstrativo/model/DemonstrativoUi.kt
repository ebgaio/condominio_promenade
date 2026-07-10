package br.com.edificiopromenade.presentation.demonstrativo.model

data class DemonstrativoUi(

    val id: Long = 0,

    val fechamentoId: Long = 0,

    val apartamentoId: Long = 0,

    val numeroApartamento: String = "",

    val nomeMorador: String = "",

    val percentualCopasa: Double = 0.0,

    val rateioMensal: Double = 0.0,

    val copasa: Double = 0.0,

    val fundoReserva: Double = 0.0,

    val decimoTerceiroFerias: Double = 0.0,

    val total: Double = 0.0
)