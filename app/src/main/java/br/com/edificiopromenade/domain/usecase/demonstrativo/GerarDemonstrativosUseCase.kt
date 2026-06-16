package br.com.edificiopromenade.domain.usecase.demonstrativo

import br.com.edificiopromenade.data.local.entity.DemonstrativoApartamentoEntity
import br.com.edificiopromenade.domain.repository.DemonstrativoRepository
import br.com.edificiopromenade.domain.usecase.fechamento.CalcularRateioMensalUseCase
import jakarta.inject.Inject

class GerarDemonstrativosUseCase @Inject constructor(
    private val demonstrativoRepository: DemonstrativoRepository,
    private val calcularRateioMensalUseCase: CalcularRateioMensalUseCase
) {
    suspend operator fun invoke(
        fechamentoId: Long
    ): List<DemonstrativoApartamentoEntity> {

        val demonstrativosCalculados =
            calcularRateioMensalUseCase(
                fechamentoId
            )

        if (demonstrativosCalculados.isEmpty())
            return emptyList()

        val entidades =
            demonstrativosCalculados.map {

                DemonstrativoApartamentoEntity(

                    fechamentoId = fechamentoId,

                    apartamentoId = it.apartamentoId,

                    nomeMoradorHistorico = it.nomeMoradorHistorico,

                    percentualCopasaHistorica = it.percentualCopasaHistorica,

                    numeroApartamentoHistorico = it.numeroApartamentoHistorico,

                    rateioMensal = it.valorRateio,

                    copasa = it.valorCopasa,

                    fundoReserva = it.valorReserva,

                    decimoTerceiroFerias = it.valorDecimoTerceiro,

                    total = it.valorTotal
                )
            }

        demonstrativoRepository
            .excluirPorFechamento(
                fechamentoId
            )

        demonstrativoRepository
            .salvarTodos(
                entidades
            )

        return entidades
    }
}
