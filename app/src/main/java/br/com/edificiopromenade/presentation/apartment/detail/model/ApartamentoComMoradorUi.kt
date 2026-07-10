package br.com.edificiopromenade.presentation.apartment.detail.model

import br.com.edificiopromenade.presentation.apartment.model.ApartamentoUi
import br.com.edificiopromenade.presentation.resident.model.MoradorUi

data class ApartamentoComMoradorUi(

    val apartamento: ApartamentoUi,

    val moradores: List<MoradorUi>

)