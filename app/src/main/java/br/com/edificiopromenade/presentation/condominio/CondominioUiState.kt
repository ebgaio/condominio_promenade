package br.com.edificiopromenade.presentation.condominio

import br.com.edificiopromenade.presentation.common.message.UiMessage
import br.com.edificiopromenade.presentation.condominio.model.CondominioUi

data class CondominioUiState(

    val condominio: CondominioUi = CondominioUi(),

    val carregando: Boolean = false,

    val mensagem: UiMessage? = null
)