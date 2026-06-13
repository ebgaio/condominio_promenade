package br.com.edificiopromenade.presentation.fechamento

import br.com.edificiopromenade.data.local.entity.FechamentoMensalEntity

data class NovoFechamentoUiState(

    val mes: String = "",

    val ano: String = "",

    val carregando: Boolean = false,

    val mensagem: String? = null,

    val fechamentos: List<FechamentoMensalEntity> = emptyList()
)