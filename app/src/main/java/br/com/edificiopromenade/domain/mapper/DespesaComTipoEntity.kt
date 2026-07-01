package br.com.edificiopromenade.domain.mapper

import br.com.edificiopromenade.data.local.entity.DespesaComTipoEntity
import br.com.edificiopromenade.domain.model.DespesaCalculavel

fun DespesaComTipoEntity.toModel() =

    DespesaCalculavel(

        id = despesa.id,

        descricao = tipo.descricao,

        valor = despesa.valor,

        usaFracaoIdeal = tipo.usaFracaoIdeal
    )