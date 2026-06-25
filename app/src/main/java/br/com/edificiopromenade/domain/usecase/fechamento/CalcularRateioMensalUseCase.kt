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
            .findListByFechamento(
                fechamentoId
            )

        val apartamentos = apartamentoRepository
            .findAllAtivosList()

        if (apartamentos.isEmpty())
            return emptyList()

        val valorCopasaTotal = despesas
            .firstOrNull {
                it.descricao.equals("COPASA", ignoreCase = true)
            }
            ?.valor
            ?: 0.0

        val despesasRateio = despesas
            .filterNot {
                it.descricao.equals("COPASA", ignoreCase = true)
            }

        val totalRateio =
            despesasRateio.sumOf {
                it.valor
            }

        val despesaCopasa = despesas
            .firstOrNull {
                it.descricao.equals( "COPASA", ignoreCase = true)
            }

        require(
            despesaCopasa != null
        ) {
            "Despesa COPASA não cadastrada."
        }

        val quantidade = apartamentos.size

        val valorRateioIndividual = if (quantidade > 0) totalRateio / quantidade else 0.00

        val valorReservaIndividual = if (quantidade > 0) fechamento.valorFundoReserva / quantidade else 0.00

        val valorDecimoIndividual = if (quantidade > 0) fechamento.valorDecimoTerceiroFerias / quantidade else 0.00

        return apartamentos.map { apartamento ->

            val morador = moradorRepository
                    .findMoradorAtivoPorApartamento(
                        apartamento.id
                    )

            val nomeMorador = morador?.nome ?: "Sem Morador"

            val valorCopasaIndividual = valorCopasaTotal * apartamento.percentualCopasa

            val total = valorRateioIndividual +
                        valorCopasaIndividual +
                        valorReservaIndividual +
                        valorDecimoIndividual

            DemonstrativoCalculado(
                apartamentoId = apartamento.id,
                numeroApartamentoHistorico = apartamento.numero,
                nomeMoradorHistorico = nomeMorador,
                percentualCopasaHistorica = apartamento.percentualCopasa,
                valorRateio = valorRateioIndividual,
                valorCopasa = valorCopasaIndividual,
                valorReserva = valorReservaIndividual,
                valorDecimoTerceiro = valorDecimoIndividual,
                valorTotal = total
            )
        }
    }
}