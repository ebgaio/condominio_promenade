package br.com.edificiopromenade.domain.usecase.historico

import br.com.edificiopromenade.domain.repository.DemonstrativoRepository
import jakarta.inject.Inject

class ConsultarDemonstrativosPorApartamentoUseCase @Inject constructor(
    private val repository: DemonstrativoRepository
) {
    suspend operator fun invoke(
        apartamentoId: Long
    ) = repository.consultarPorApartamento(
        apartamentoId
    )
}
