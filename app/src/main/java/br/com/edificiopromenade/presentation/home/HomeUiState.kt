package br.com.edificiopromenade.presentation.home

import br.com.edificiopromenade.presentation.common.message.UiMessage

data class HomeUiState(

    val carregando: Boolean = false,

    val mensagem: UiMessage? = null

)