package br.com.edificiopromenade.domain.usecase.apartamento

import br.com.edificiopromenade.data.local.entity.ApartamentoEntity
import br.com.edificiopromenade.domain.repository.ApartamentoRepository
import jakarta.inject.Inject

class CadastrarApartamentoUseCase @Inject constructor(
    private val repository: ApartamentoRepository
) {

    suspend operator fun invoke(
        apartamento: ApartamentoEntity
    ): Long {
        return repository.insert(apartamento)
    }
}