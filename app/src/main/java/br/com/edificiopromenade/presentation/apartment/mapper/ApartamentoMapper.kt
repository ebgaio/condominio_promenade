package br.com.edificiopromenade.presentation.apartment.mapper

import br.com.edificiopromenade.data.local.entity.ApartamentoEntity
import br.com.edificiopromenade.presentation.apartment.model.ApartamentoUi

fun ApartamentoEntity.toUi() =
    ApartamentoUi(
        id = id,
        condominioId = condominioId,
        numero = numero,
        percentualCopasa = percentualCopasa,
        ativo = ativo
    )

fun ApartamentoUi.toEntity() =
    ApartamentoEntity(
        id = id,
        condominioId = condominioId,
        numero = numero,
        percentualCopasa = percentualCopasa,
        ativo = ativo
    )