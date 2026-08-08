package br.com.edificiopromenade.presentation.despesatemporaria.model

data class DespesaTemporariaParcelaUi(

    val id: Long = 0,

    val despesaTemporariaId: Long,

    val competencia: Int,

    val numeroParcela: Int,

    val valor: Double,

    val fechamentoId: Long? = null,

    val despesaId: Long? = null,

    val paga: Boolean = false
)