package br.com.edificiopromenade.domain.usecase.morador

import br.com.edificiopromenade.domain.repository.MoradorRepository
import jakarta.inject.Inject

class ConsultarHistoricoMoradoresUseCase @Inject constructor(
    private val repository: MoradorRepository
) {

    suspend operator fun invoke(
        apartamentoId: Long
    ) =
        repository.findHistoricoPorApartamento(
            apartamentoId
        )
}