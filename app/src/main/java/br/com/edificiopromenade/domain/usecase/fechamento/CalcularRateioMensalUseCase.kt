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
            .findComTipoByFechamento(
                fechamentoId
            )

        val apartamentos = apartamentoRepository
            .findAllAtivosList()

        if (apartamentos.isEmpty())
            return emptyList()

        val despesasFracaoIdeal =
            despesas.filter {
                it.tipo.usaFracaoIdeal
            }

        val despesasRateioIgual =
            despesas.filter {
                !it.tipo.usaFracaoIdeal
            }

        val totalCopasa =
            despesasFracaoIdeal.sumOf {
                it.despesa.valor
            }

        val totalRateio =
            despesasRateioIgual.sumOf {
                it.despesa.valor
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

            val valorCopasaIndividual = totalCopasa * apartamento.percentualCopasa

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