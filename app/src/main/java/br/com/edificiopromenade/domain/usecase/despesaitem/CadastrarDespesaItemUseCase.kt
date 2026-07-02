package br.com.edificiopromenade.domain.usecase.despesaitem

import br.com.edificiopromenade.data.local.entity.DespesaItemEntity
import br.com.edificiopromenade.domain.repository.DespesaItemRepository
import jakarta.inject.Inject

class CadastrarDespesaItemUseCase @Inject constructor(

    private val repository: DespesaItemRepository

) {

    suspend operator fun invoke(
        item: DespesaItemEntity
    ) =
        repository.insert(item)
}