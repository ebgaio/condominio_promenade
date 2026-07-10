package br.com.edificiopromenade.presentation.moradores

import br.com.edificiopromenade.presentation.apartment.detail.model.ApartamentoComMoradorUi
import br.com.edificiopromenade.presentation.common.message.UiMessage

data class MoradoresUiState(

    val isLoading: Boolean = false,

    val apartamentos: List<ApartamentoComMoradorUi> = emptyList(),

    val apartamentoIdSelecionado: Long = 0,

    val nome: String = "",

    val salvoComSucesso: Boolean = false,

    val mensagem: UiMessage? = null,

    val modoEdicao: Boolean = false,

    val moradorSelecionadoId: Long = 0

)