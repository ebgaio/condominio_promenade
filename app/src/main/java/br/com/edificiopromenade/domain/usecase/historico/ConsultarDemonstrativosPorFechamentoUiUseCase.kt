package br.com.edificiopromenade.domain.usecase.historico

import br.com.edificiopromenade.domain.repository.DemonstrativoRepository
import br.com.edificiopromenade.presentation.history.mapper.toUi
import jakarta.inject.Inject

class ConsultarDemonstrativosPorFechamentoUiUseCase @Inject constructor(
    private val repository: DemonstrativoRepository
) {

    suspend operator fun invoke(
        fechamentoId: Long
    ) = repository
            .consultarPorFechamento(fechamentoId)
            .map {
                it.toUi()
            }
}