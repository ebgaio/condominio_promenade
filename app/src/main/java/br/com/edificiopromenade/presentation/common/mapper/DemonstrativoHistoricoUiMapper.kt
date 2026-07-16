package br.com.edificiopromenade.presentation.common.mapper

import br.com.edificiopromenade.data.local.entity.DemonstrativoApartamentoEntity
import br.com.edificiopromenade.presentation.history.model.DemonstrativoHistoricoUi

fun DemonstrativoApartamentoEntity.toUi() =

    DemonstrativoHistoricoUi(

        id = id,

        fechamentoId = fechamentoId,

        apartamentoId = apartamentoId,

        nomeMorador = nomeMoradorHistorico,

        percentualCopasa = percentualCopasaHistorica,

        numeroApartamento = numeroApartamentoHistorico,

        rateioMensal = rateioMensal,

        copasa = copasa,

        fundoReserva = fundoReserva,

        decimoTerceiroFerias = decimoTerceiroFerias,

        total = total

    )

fun DemonstrativoHistoricoUi.toEntity() =

    DemonstrativoApartamentoEntity(

        id = id,

        fechamentoId = fechamentoId,

        apartamentoId = apartamentoId,

        nomeMoradorHistorico = nomeMorador,

        percentualCopasaHistorica = percentualCopasa,

        numeroApartamentoHistorico = numeroApartamento,

        rateioMensal = rateioMensal,

        copasa = copasa,

        fundoReserva = fundoReserva,

        decimoTerceiroFerias = decimoTerceiroFerias,

        total = total

    )
