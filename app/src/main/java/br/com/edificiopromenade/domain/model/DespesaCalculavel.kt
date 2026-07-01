package br.com.edificiopromenade.domain.model

data class DespesaCalculavel(

    val id: Long,

    val descricao: String,

    val valor: Double,

    val usaFracaoIdeal: Boolean
)