package br.com.edificiopromenade.domain.usecase.morador

import br.com.edificiopromenade.domain.repository.MoradorRepository
import jakarta.inject.Inject

class ConsultarMoradorPorIdUseCase @Inject constructor(
    private val repository: MoradorRepository
) {

    suspend operator fun invoke(
        id: Long
    ) = repository.findById(id)
}