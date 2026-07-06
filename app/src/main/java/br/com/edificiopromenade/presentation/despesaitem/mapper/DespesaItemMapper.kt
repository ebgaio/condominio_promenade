package br.com.edificiopromenade.presentation.despesaitem.mapper

import br.com.edificiopromenade.data.local.entity.DespesaItemEntity
import br.com.edificiopromenade.presentation.despesaitem.model.DespesaItemUi

fun DespesaItemEntity.toUi() =

    DespesaItemUi(

        id = id,

        despesaId = despesaId,

        descricao = descricao,

        valor = valor
    )

fun DespesaItemUi.toEntity() =

    DespesaItemEntity(

        id = id,

        despesaId = despesaId,

        descricao = descricao,

        valor = valor
    )
