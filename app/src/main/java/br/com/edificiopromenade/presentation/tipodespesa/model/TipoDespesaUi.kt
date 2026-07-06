package br.com.edificiopromenade.presentation.tipodespesa.model

data class TipoDespesaUi(

    val id: Long,

    val descricao: String,

    val recorrente: Boolean,

    val usaFracaoIdeal: Boolean = false,
)