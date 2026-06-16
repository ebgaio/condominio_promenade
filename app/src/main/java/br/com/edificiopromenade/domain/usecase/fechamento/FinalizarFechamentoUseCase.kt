package br.com.edificiopromenade.domain.usecase.fechamento

import br.com.edificiopromenade.domain.repository.FechamentoRepository
import br.com.edificiopromenade.domain.usecase.demonstrativo.GerarDemonstrativosUseCase
import jakarta.inject.Inject

class FinalizarFechamentoUseCase @Inject constructor(
    private val fechamentoRepository: FechamentoRepository,
    private val gerarDemonstrativosUseCase: GerarDemonstrativosUseCase

) {
    suspend operator fun invoke(
        fechamentoId: Long
    ) {
        val fechamento =fechamentoRepository.findById(
                fechamentoId
            )
                ?: return

        gerarDemonstrativosUseCase(
            fechamentoId = fechamentoId
        )

        fechamentoRepository.update(
            fechamento.copy(
                finalizado = true
            )
        )
    }
}