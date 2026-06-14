package br.com.edificiopromenade.domain.usecase.fechamento

import br.com.edificiopromenade.data.local.entity.DemonstrativoApartamentoEntity
import br.com.edificiopromenade.domain.model.DemonstrativoCalculado
import br.com.edificiopromenade.domain.repository.DemonstrativoRepository
import javax.inject.Inject

class GerarDemonstrativosUseCase @Inject constructor(
    private val demonstrativoRepository: DemonstrativoRepository
) {
    suspend operator fun invoke(
        fechamentoId: Long,
        demonstrativos: List<DemonstrativoCalculado>
    ) {
        demonstrativoRepository
            .deletePorFechamento(
                fechamentoId
            )

        demonstrativos.forEach {

            demonstrativoRepository.insert(

                DemonstrativoApartamentoEntity(

                    fechamentoId = fechamentoId,

                    apartamentoId = it.apartamentoId,

                    numeroApartamentoHistorico = it.numeroApartamentoHistorico,

                    nomeMoradorHistorico = it.nomeMoradorHistorico,

                    percentualCopasaHistorica = it.percentualCopasaHistorica,

                    rateioMensal = it.valorRateio,

                    copasa = it.valorCopasa,

                    fundoReserva = it.valorReserva,

                    decimoTerceiroFerias = it.valorDecimoTerceiro,

                    total = it.valorTotal
                )
            )
        }
    }
}