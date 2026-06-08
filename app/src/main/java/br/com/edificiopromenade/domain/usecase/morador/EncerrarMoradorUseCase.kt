package br.com.edificiopromenade.domain.usecase.morador

import br.com.edificiopromenade.domain.repository.MoradorRepository
import java.time.LocalDate
import javax.inject.Inject

class EncerrarMoradorUseCase @Inject constructor(
    private val repository: MoradorRepository
) {

    suspend operator fun invoke(
        id: Long,
        dataFim: LocalDate
    ) {
        repository.encerrarMorador(
            id,
            dataFim
        )
    }
}