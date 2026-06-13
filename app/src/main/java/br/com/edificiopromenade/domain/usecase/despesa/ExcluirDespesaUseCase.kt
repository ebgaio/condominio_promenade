package br.com.edificiopromenade.domain.usecase.despesa

import br.com.edificiopromenade.data.local.entity.DespesaEntity
import br.com.edificiopromenade.domain.repository.DespesaRepository
import jakarta.inject.Inject

class ExcluirDespesaUseCase @Inject constructor(
    private val repository: DespesaRepository
) {

    suspend operator fun invoke(
        despesa: DespesaEntity
    ) {

        repository.delete(despesa)
    }
}