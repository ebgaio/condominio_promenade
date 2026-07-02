package br.com.edificiopromenade.presentation.tipodespesa.model

data class TipoDespesaItemUi(

    val id: Long,

    val descricao: String,

    val recorrente: Boolean,

    val usaFracaoIdeal: Boolean
)