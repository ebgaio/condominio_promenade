package br.com.edificiopromenade.domain.usecase.morador

import br.com.edificiopromenade.data.local.entity.MoradorEntity
import br.com.edificiopromenade.domain.repository.MoradorRepository
import jakarta.inject.Inject

class AlterarMoradorUseCase @Inject constructor(
    private val repository: MoradorRepository
) {

    suspend operator fun invoke(
        morador: MoradorEntity
    ) {
        repository.update(morador)
    }
}