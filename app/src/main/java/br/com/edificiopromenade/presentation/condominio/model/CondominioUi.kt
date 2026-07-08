package br.com.edificiopromenade.presentation.condominio.model

import java.time.LocalDateTime

data class CondominioUi(

    val id: Long = 0,

    val nome: String = "",

    val cnpj: String = "",

    val endereco: String = "",

    val nomeAdministradora: String = "",

    val emailAdministradora: String = "",

    val ativo: Boolean = true,

    val dataCriacao: LocalDateTime = LocalDateTime.now(),

    val dataInativacao: LocalDateTime? = null
)