package br.com.edificiopromenade.domain.usecase.despesaitem

import br.com.edificiopromenade.domain.repository.DespesaItemRepository
import jakarta.inject.Inject

class CalcularTotalItensDespesaUseCase @Inject constructor(

    private val repository: DespesaItemRepository

) {

    suspend operator fun invoke(
        despesaId: Long
    ) =
        repository.calcularTotalItens(despesaId)
}