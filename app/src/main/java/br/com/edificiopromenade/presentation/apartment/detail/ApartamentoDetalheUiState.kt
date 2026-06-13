package br.com.edificiopromenade.presentation.apartment.detail

import br.com.edificiopromenade.data.local.relation.ApartamentoDetalhado

data class ApartamentoDetalheUiState(

    val carregando: Boolean = true,

    val apartamento: ApartamentoDetalhado? = null,

    val numero: String = "",

    val fracaoIdealAtual: String = "",

    val modoEdicao: Boolean = false,

    val mensagem: String? = null,

    val totalMoradoresAtivos: Int = 0,

    val totalMoradoresHistorico: Int = 0
)