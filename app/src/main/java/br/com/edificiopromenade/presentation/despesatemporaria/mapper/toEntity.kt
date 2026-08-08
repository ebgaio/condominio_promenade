package br.com.edificiopromenade.presentation.despesatemporaria.mapper

import br.com.edificiopromenade.data.local.entity.DespesaTemporariaParcelaEntity
import br.com.edificiopromenade.presentation.despesatemporaria.model.DespesaTemporariaParcelaUi

fun DespesaTemporariaParcelaUi.toEntity() =

    DespesaTemporariaParcelaEntity(

        id = id,

        despesaTemporariaId = despesaTemporariaId,

        competencia = competencia,

        numeroParcela = numeroParcela,

        valor = valor,

        fechamentoId = fechamentoId,

        despesaId = despesaId,

        paga = paga
    )