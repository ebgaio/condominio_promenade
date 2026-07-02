package br.com.edificiopromenade.presentation.tipodespesa.mapper

import br.com.edificiopromenade.data.local.entity.TipoDespesaEntity
import br.com.edificiopromenade.presentation.tipodespesa.model.TipoDespesaItemUi

fun TipoDespesaEntity.toUi() =

    TipoDespesaItemUi(

        id = id,

        descricao = descricao,

        recorrente = recorrente,

        usaFracaoIdeal = usaFracaoIdeal
    )