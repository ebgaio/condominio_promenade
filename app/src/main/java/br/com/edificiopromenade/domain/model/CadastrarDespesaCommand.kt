package br.com.edificiopromenade.domain.model

data class CadastrarDespesaCommand(

    val fechamentoId: Long,

    val tipoDespesaId: Long,

    val descricaoLivre: String,

    val valor: Double
)