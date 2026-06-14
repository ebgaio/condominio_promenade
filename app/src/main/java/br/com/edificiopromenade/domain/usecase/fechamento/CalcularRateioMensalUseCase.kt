package br.com.edificiopromenade.domain.usecase.fechamento

import br.com.edificiopromenade.domain.model.DemonstrativoCalculado
import br.com.edificiopromenade.domain.repository.ApartamentoRepository
import br.com.edificiopromenade.domain.repository.DespesaRepository
import br.com.edificiopromenade.domain.repository.FechamentoRepository
import br.com.edificiopromenade.domain.repository.MoradorRepository
import jakarta.inject.Inject

class CalcularRateioMensalUseCase @Inject constructor(
    private val fechamentoRepository: FechamentoRepository,
    private val despesaRepository: DespesaRepository,
    private val apartamentoRepository: ApartamentoRepository,
    private val moradorRepository: MoradorRepository
) {

    suspend operator fun invoke(
        fechamentoId: Long
    ): List<DemonstrativoCalculado> {

        val fechamento = fechamentoRepository
            .findComDespesas(fechamentoId)
            ?.fechamento
            ?: return emptyList()

        val despesas = despesaRepository
            .findByFechamentoList(
                fechamentoId
            )

        val apartamentos = apartamentoRepository
            .findAllAtivosList()

        if (apartamentos.isEmpty())
            return emptyList()

        val valorCopasaTotal = despesas
            .firstOrNull {
                it.descricao.equals(
                    "COPASA",
                    ignoreCase = true
                )
            }
            ?.valor
            ?: 0.0

        val totalRateio = despesas
            .filterNot {
                it.descricao.equals(
                    "COPASA",
                    ignoreCase = true
                )
            }
            .sumOf {
                it.valor
            }

        val quantidade = apartamentos.size

        val valorRateio = totalRateio / quantidade

        val valorReserva = fechamento.valorFundoReserva / quantidade

        val valorDecimo = fechamento.valorDecimoTerceiroFerias / quantidade

        return apartamentos.map { apartamento ->

            val morador = moradorRepository
                    .findMoradorAtivoPorApartamento(
                        apartamento.id
                    )

            val nomeMorador =
                morador?.nome ?: "Sem Morador"

            val valorCopasa = valorCopasaTotal * apartamento.percentualCopasa / 100.0

            val total = valorRateio +
                        valorCopasa +
                        valorReserva +
                        valorDecimo

            DemonstrativoCalculado(

                apartamentoId = apartamento.id,

                numeroApartamentoHistorico = apartamento.numero,

                nomeMoradorHistorico = nomeMorador,

                percentualCopasaHistorica = apartamento.percentualCopasa,

                valorRateio = valorRateio,

                valorCopasa = valorCopasa,

                valorReserva = valorReserva,

                valorDecimoTerceiro = valorDecimo,

                valorTotal = total
            )
        }
    }
}