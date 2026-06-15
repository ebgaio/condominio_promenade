package br.com.edificiopromenade.domain.usecase.fechamento

import br.com.edificiopromenade.domain.repository.FechamentoRepository
import javax.inject.Inject

class FinalizarFechamentoUseCase @Inject constructor(
    private val fechamentoRepository: FechamentoRepository,
    private val calcularRateioMensalUseCase: CalcularRateioMensalUseCase,
    private val gerarDemonstrativosUseCase: GerarDemonstrativosUseCase
) {
    suspend operator fun invoke(
        fechamentoId: Long
    ) {
        val fechamento =fechamentoRepository.findById(
                fechamentoId
            )
                ?: return

        val demonstrativos = calcularRateioMensalUseCase(
            fechamentoId
        )

        gerarDemonstrativosUseCase(
            fechamentoId = fechamentoId,
            demonstrativos = demonstrativos
        )

        fechamentoRepository.update(
            fechamento.copy(
                finalizado = true
            )
        )
    }
}