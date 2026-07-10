package br.com.edificiopromenade.domain.usecase.historico

import br.com.edificiopromenade.domain.repository.DemonstrativoRepository
import br.com.edificiopromenade.presentation.history.mapper.toUi
import jakarta.inject.Inject

class ConsultarDemonstrativosPorApartamentoUiUseCase @Inject constructor(
    private val repository: DemonstrativoRepository
) {

    suspend operator fun invoke(
        apartamentoId: Long
    ) = repository
            .consultarPorApartamento(apartamentoId)
            .map {
                it.toUi()
            }
}