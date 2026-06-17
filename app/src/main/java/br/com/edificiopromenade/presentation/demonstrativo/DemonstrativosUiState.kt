package br.com.edificiopromenade.presentation.demonstrativo

import br.com.edificiopromenade.data.local.entity.DemonstrativoApartamentoEntity

data class DemonstrativosUiState(

    val demonstrativos:
        List<DemonstrativoApartamentoEntity> =
            emptyList(),

    val totalGeral: Double = 0.0,

    val carregando: Boolean = false
)