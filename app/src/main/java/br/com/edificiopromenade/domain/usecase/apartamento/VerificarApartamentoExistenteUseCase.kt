package br.com.edificiopromenade.domain.usecase.apartamento

import br.com.edificiopromenade.domain.repository.ApartamentoRepository
import jakarta.inject.Inject

class VerificarApartamentoExistenteUseCase @Inject constructor(
    private val repository: ApartamentoRepository
) {
    suspend operator fun invoke(
        numero: String
    ): Boolean {
        return repository.existeApartamento(
            numero
        )
    }
}