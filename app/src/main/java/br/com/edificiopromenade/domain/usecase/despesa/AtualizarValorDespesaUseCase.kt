package br.com.edificiopromenade.domain.usecase.despesa

import br.com.edificiopromenade.domain.repository.DespesaRepository
import jakarta.inject.Inject

class AtualizarValorDespesaUseCase @Inject constructor(
    private val repository: DespesaRepository
) {
    suspend operator fun invoke(
        despesaId: Long,
        novoValor: Double
    ) {
        repository.atualizarValor(
            despesaId,
            novoValor
        )
    }
}