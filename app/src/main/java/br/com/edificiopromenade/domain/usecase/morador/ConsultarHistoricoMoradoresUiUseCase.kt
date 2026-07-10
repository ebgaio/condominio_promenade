package br.com.edificiopromenade.domain.usecase.morador

import br.com.edificiopromenade.domain.repository.MoradorRepository
import br.com.edificiopromenade.presentation.resident.mapper.toUi
import br.com.edificiopromenade.presentation.resident.model.MoradorUi
import jakarta.inject.Inject

class ConsultarHistoricoMoradoresUiUseCase @Inject constructor(
    private val repository: MoradorRepository
) {
    suspend operator fun invoke(
        apartamentoId: Long
    ): List<MoradorUi> {

        return repository
            .findHistoricoPorApartamento(apartamentoId)
            .map {
                it.toUi()
            }
    }
}