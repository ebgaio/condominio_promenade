package br.com.edificiopromenade.presentation.demonstrativo

import br.com.edificiopromenade.presentation.common.message.UiMessage
import br.com.edificiopromenade.presentation.demonstrativo.model.DemonstrativoUi

data class DemonstrativosUiState(

    val fechamentoId: Long? = null,

    val demonstrativos: List<DemonstrativoUi> = emptyList(),

    val totalGeral: Double = 0.0,

    val carregando: Boolean = false,

    val mensagem: UiMessage? = null
)