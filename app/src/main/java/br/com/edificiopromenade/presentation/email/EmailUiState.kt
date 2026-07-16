package br.com.edificiopromenade.presentation.email

import br.com.edificiopromenade.presentation.common.message.UiMessage
import br.com.edificiopromenade.presentation.email.model.EmailUi

data class EmailUiState(

    val fechamentoId: Long? = null,

    val email: EmailUi? = null,

    val carregando: Boolean = false,

    val mensagem: UiMessage? = null

)