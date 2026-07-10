package br.com.edificiopromenade.presentation.resident.mapper

import br.com.edificiopromenade.data.local.entity.MoradorEntity
import br.com.edificiopromenade.presentation.resident.model.MoradorUi

fun MoradorEntity.toUi() =

    MoradorUi(

        id = id,

        apartamentoId = apartamentoId,

        nome = nome,

        dataInicio = dataInicio,

        dataFim = dataFim,

        ativo = ativo
    )

fun MoradorUi.toEntity() =

    MoradorEntity(

        id = id,

        apartamentoId = apartamentoId,

        nome = nome,

        dataInicio = dataInicio,

        dataFim = dataFim,

        ativo = ativo
    )