package br.com.edificiopromenade.presentation.demonstrativo

import br.com.edificiopromenade.data.local.entity.DemonstrativoApartamentoEntity
import br.com.edificiopromenade.presentation.common.message.UiMessage

data class DemonstrativosUiState(

    val fechamentoId: Long? = null,

    val demonstrativos:
        List<DemonstrativoApartamentoEntity> =
            emptyList(),

    val totalGeral: Double = 0.0,

    val carregando: Boolean = false,

    val mensagem: UiMessage? = null
)