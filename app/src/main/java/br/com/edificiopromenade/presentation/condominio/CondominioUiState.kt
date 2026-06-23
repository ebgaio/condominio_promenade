package br.com.edificiopromenade.presentation.condominio

import br.com.edificiopromenade.presentation.common.message.UiMessage

data class CondominioUiState(

    val nome: String = "",

    val cnpj: String = "",

    val endereco: String = "",

    val nomeAdministradora: String = "",

    val emailAdministradora: String = "",

    val carregando: Boolean = false,

    val salvoComSucesso: Boolean = false,

    val mensagem: UiMessage? = null
)