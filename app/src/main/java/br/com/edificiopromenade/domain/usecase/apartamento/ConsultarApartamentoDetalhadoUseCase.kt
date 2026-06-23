package br.com.edificiopromenade.domain.usecase.apartamento

import br.com.edificiopromenade.domain.repository.ApartamentoRepository
import jakarta.inject.Inject

class ConsultarApartamentoDetalhadoUseCase @Inject constructor(
    private val repository: ApartamentoRepository
) {
    suspend operator fun invoke(
        id: Long
    ) = repository.findDetalhado(id)
}