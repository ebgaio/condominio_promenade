package br.com.edificiopromenade.presentation.common.mapper

import br.com.edificiopromenade.data.local.entity.TipoDespesaEntity
import br.com.edificiopromenade.presentation.common.model.DespesaItemUi

fun TipoDespesaEntity.toUi() =

    DespesaItemUi(

        id = id,

        descricao = descricao,

        recorrente = recorrente,

        usaFracaoIdeal = usaFracaoIdeal
    )