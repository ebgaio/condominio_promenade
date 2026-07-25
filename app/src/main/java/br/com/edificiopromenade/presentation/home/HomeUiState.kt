package br.com.edificiopromenade.presentation.home

import br.com.edificiopromenade.domain.navigation.EstadoSistema
import br.com.edificiopromenade.presentation.common.message.UiMessage

data class HomeUiState(

    val estadoSistema: EstadoSistema? = null,

    val carregando: Boolean = false,

    val mensagem: UiMessage? = null

)