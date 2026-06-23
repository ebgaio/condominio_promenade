package br.com.edificiopromenade.presentation.apartment.detail

import br.com.edificiopromenade.data.local.relation.ApartamentoDetalhado
import br.com.edificiopromenade.presentation.common.message.UiMessage

data class ApartamentoDetalheUiState(

    val carregando: Boolean = true,

    val apartamento: ApartamentoDetalhado? = null,

    val numero: String = "",

    val percentualCopasa: String = "",

    val modoEdicao: Boolean = false,

    val mensagem: UiMessage? = null,

    val totalMoradoresAtivos: Int = 0,

    val totalMoradoresHistorico: Int = 0
)