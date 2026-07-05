package br.com.edificiopromenade.presentation.common.mapper

import br.com.edificiopromenade.data.local.entity.DespesaComTipoEntity
import br.com.edificiopromenade.presentation.common.model.DespesaUi

fun DespesaComTipoEntity.toUi() =

    DespesaUi(

        id = despesa.id,

        descricao = tipo.descricao,

        valor = despesa.valor
    )