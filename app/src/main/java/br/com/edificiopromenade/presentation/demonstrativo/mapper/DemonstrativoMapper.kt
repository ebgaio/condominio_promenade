package br.com.edificiopromenade.presentation.demonstrativo.mapper

import br.com.edificiopromenade.data.local.entity.DemonstrativoApartamentoEntity
import br.com.edificiopromenade.presentation.demonstrativo.model.DemonstrativoUi

fun DemonstrativoApartamentoEntity.toUi() =

    DemonstrativoUi(

        id = id,

        fechamentoId = fechamentoId,

        apartamentoId = apartamentoId,

        numeroApartamento = numeroApartamentoHistorico,

        nomeMorador = nomeMoradorHistorico,

        percentualCopasa = percentualCopasaHistorica,

        rateioMensal = rateioMensal,

        copasa = copasa,

        fundoReserva = fundoReserva,

        decimoTerceiroFerias = decimoTerceiroFerias,

        total = total

    )

fun DemonstrativoUi.toEntity() =

    DemonstrativoApartamentoEntity(

        id = id,

        fechamentoId = fechamentoId,

        apartamentoId = apartamentoId,

        numeroApartamentoHistorico = numeroApartamento,

        nomeMoradorHistorico = nomeMorador,

        percentualCopasaHistorica = percentualCopasa,

        rateioMensal = rateioMensal,

        copasa = copasa,

        fundoReserva = fundoReserva,

        decimoTerceiroFerias = decimoTerceiroFerias,

        total = total

    )