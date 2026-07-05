package br.com.edificiopromenade.presentation.common.mapper

import br.com.edificiopromenade.data.local.entity.DespesaItemEntity
import br.com.edificiopromenade.presentation.common.model.DespesaUi

fun DespesaItemEntity.toUi() =

    DespesaUi(

        id = id,

        descricao = descricao,

        valor = valor
    )
