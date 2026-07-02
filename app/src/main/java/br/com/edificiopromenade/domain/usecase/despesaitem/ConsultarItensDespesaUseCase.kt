package br.com.edificiopromenade.domain.usecase.despesaitem

import br.com.edificiopromenade.domain.repository.DespesaItemRepository
import jakarta.inject.Inject

class ConsultarItensDespesaUseCase @Inject constructor(

    private val repository: DespesaItemRepository

) {

    operator fun invoke(
        despesaId: Long
    ) =
        repository.findByDespesa(despesaId)
}