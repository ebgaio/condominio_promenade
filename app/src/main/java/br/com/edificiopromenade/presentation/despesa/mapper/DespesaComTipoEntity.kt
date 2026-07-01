package br.com.edificiopromenade.presentation.despesa.mapper

import br.com.edificiopromenade.data.local.entity.DespesaComTipoEntity
import br.com.edificiopromenade.presentation.despesa.model.DespesaItemUi

fun DespesaComTipoEntity.toUi() =

    DespesaItemUi(

        id = despesa.id,

        descricao = tipo.descricao,

        valor = despesa.valor
    )