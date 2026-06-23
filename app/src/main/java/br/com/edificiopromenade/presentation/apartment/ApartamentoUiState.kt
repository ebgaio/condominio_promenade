package br.com.edificiopromenade.presentation.apartment

import br.com.edificiopromenade.data.local.entity.ApartamentoEntity
import br.com.edificiopromenade.presentation.common.message.UiMessage

data class ApartamentoUiState(

    val id: Long = 0,

    val numero: String = "",

    val percentualCopasa: String = "",

    val ativo: Boolean = true,

    val salvoComSucesso: Boolean = false,

    val apartamentos: List<ApartamentoEntity> = emptyList(),

    val modoEdicao: Boolean = false,

    val apartamentoSelecionadoId: Long = 0,

    val condominioIdSelecionado: Long = 0,

    val mensagem: UiMessage? = null
)