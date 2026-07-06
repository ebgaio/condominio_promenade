package br.com.edificiopromenade.presentation.tipodespesa.mapper

import br.com.edificiopromenade.data.local.entity.TipoDespesaEntity
import br.com.edificiopromenade.presentation.tipodespesa.model.TipoDespesaUi

fun TipoDespesaEntity.toUi() =

    TipoDespesaUi(

        id = id,

        descricao = descricao,

        recorrente = recorrente,

        usaFracaoIdeal = usaFracaoIdeal
    )