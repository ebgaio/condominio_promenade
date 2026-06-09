package br.com.edificiopromenade.presentation.condominio

data class CondominioUiState(

    val nome: String = "",

    val cnpj: String = "",

    val endereco: String = "",

    val nomeAdministradora: String = "",

    val emailAdministradora: String = "",

    val carregando: Boolean = false,

    val salvoComSucesso: Boolean = false,

    val mensagemErro: String? = null,

    val mensagem: String? = null
)