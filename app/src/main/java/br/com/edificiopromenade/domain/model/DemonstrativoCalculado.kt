package br.com.edificiopromenade.domain.model

data class DemonstrativoCalculado(

    val apartamentoId: Long,

    val numeroApartamentoHistorico: String,

    val nomeMoradorHistorico: String,

    val percentualCopasaHistorica: Double,

    val valorRateio: Double,

    val valorCopasa: Double,

    val valorReserva: Double,

    val valorDecimoTerceiro: Double,

    val valorTotal: Double
)