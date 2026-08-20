package br.com.edificiopromenade.domain.usecase.despesa

import br.com.edificiopromenade.domain.repository.ExclusaoDespesaRepository
import jakarta.inject.Inject

class ExcluirParcelaDespesaAvulsaUseCase @Inject constructor(
    private val repository: ExclusaoDespesaRepository
) {

    suspend operator fun invoke(
        despesaId: Long
    ) {
        repository.excluirParcelaAvulsa(
            despesaId
        )
    }
}