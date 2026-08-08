package br.com.edificiopromenade.presentation.fechamento.mapper

import br.com.edificiopromenade.data.local.entity.FechamentoMensalEntity
import br.com.edificiopromenade.presentation.fechamento.model.FechamentoMensalUi

fun FechamentoMensalEntity.toUi() =

    FechamentoMensalUi(

        id = id,

        mes = mes,

        ano = ano,

        finalizado = finalizado
    )