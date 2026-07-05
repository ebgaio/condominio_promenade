package br.com.edificiopromenade.presentation.common.model

data class DespesaItemUi(

    val id: Long,

    val descricao: String,

    val recorrente: Boolean,

    val usaFracaoIdeal: Boolean
)