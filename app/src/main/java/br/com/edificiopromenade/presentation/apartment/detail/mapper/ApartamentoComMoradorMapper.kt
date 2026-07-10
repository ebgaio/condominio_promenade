package br.com.edificiopromenade.presentation.apartment.detail.mapper

import br.com.edificiopromenade.data.local.relation.ApartamentoComMorador
import br.com.edificiopromenade.presentation.apartment.detail.model.ApartamentoComMoradorUi
import br.com.edificiopromenade.presentation.apartment.mapper.toUi
import br.com.edificiopromenade.presentation.resident.mapper.toUi

fun ApartamentoComMorador.toUi() =
    ApartamentoComMoradorUi(
        apartamento = apartamento.toUi(),
        moradores = moradores.map {
            it.toUi()
        }
    )