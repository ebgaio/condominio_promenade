package br.com.edificiopromenade.presentation.resident.model

import java.time.LocalDate

data class MoradorUi(

    val id: Long = 0,

    val apartamentoId: Long = 0,

    val nome: String = "",

    val dataInicio: LocalDate? = null,

    val dataFim: LocalDate? = null,

    val ativo: Boolean = true

)