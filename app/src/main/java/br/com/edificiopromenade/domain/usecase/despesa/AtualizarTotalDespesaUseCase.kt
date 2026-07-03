package br.com.edificiopromenade.domain.usecase.despesa

import br.com.edificiopromenade.domain.repository.DespesaItemRepository
import br.com.edificiopromenade.domain.repository.DespesaRepository
import jakarta.inject.Inject

class AtualizarTotalDespesaUseCase @Inject constructor(
    private val despesaRepository: DespesaRepository,
    private val despesaItemRepository: DespesaItemRepository
) {

    suspend operator fun invoke(
        despesaId: Long
    ) {

        val despesa = despesaRepository
            .findById(
                despesaId
            )
            ?: return

        val total = despesaItemRepository
            .calcularTotalItens(
                despesaId
            )

        despesaRepository.update(
            despesa.copy(
                valor = total
            )
        )
    }
}