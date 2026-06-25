package br.com.edificiopromenade.domain.usecase.fechamento

import br.com.edificiopromenade.domain.repository.DespesaRepository
import br.com.edificiopromenade.domain.repository.FechamentoRepository
import br.com.edificiopromenade.domain.usecase.demonstrativo.GerarDemonstrativosUseCase
import jakarta.inject.Inject

class FinalizarFechamentoUseCase @Inject constructor(
    private val fechamentoRepository: FechamentoRepository,
    private val gerarDemonstrativosUseCase: GerarDemonstrativosUseCase,
    private val despesaRepository: DespesaRepository,
) {
    suspend operator fun invoke(
        fechamentoId: Long
    ) {
        val fechamento = fechamentoRepository.findById(
                fechamentoId
            )
                ?: return

        val despesas = despesaRepository.findListByFechamento(
                fechamentoId
            )

        if (despesas.isEmpty()) {
            throw IllegalStateException(
                "Não existem despesas cadastradas."
            )
        }

        if (fechamento.finalizado)
            return

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