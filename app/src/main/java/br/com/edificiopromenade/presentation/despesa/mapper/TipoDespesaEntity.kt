package br.com.edificiopromenade.presentation.despesa.mapper

import br.com.edificiopromenade.data.local.entity.TipoDespesaEntity
import br.com.edificiopromenade.presentation.despesa.model.TipoDespesaUi

fun TipoDespesaEntity.toUi() =

    TipoDespesaUi(

        id = id,

        descricao = descricao
    )