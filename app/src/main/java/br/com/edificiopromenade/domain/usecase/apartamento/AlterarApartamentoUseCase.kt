package br.com.edificiopromenade.domain.usecase.apartamento

import br.com.edificiopromenade.data.local.entity.ApartamentoEntity
import br.com.edificiopromenade.domain.repository.ApartamentoRepository
import javax.inject.Inject

class AlterarApartamentoUseCase @Inject constructor(
    private val repository: ApartamentoRepository
) {

    suspend operator fun invoke(
        apartamento: ApartamentoEntity
    ) {

        repository.update(apartamento)
    }
}