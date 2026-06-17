package br.com.edificiopromenade.domain.usecase.despesa

import br.com.edificiopromenade.domain.repository.DespesaRepository
import jakarta.inject.Inject

class VerificarDespesaExistenteUseCase @Inject constructor(
    private val repository: DespesaRepository
) {
    suspend operator fun invoke(
        fechamentoId: Long,
        descricao: String
    ): Boolean {
        return repository.existeDespesa(
            fechamentoId,
            descricao
        )
    }
}