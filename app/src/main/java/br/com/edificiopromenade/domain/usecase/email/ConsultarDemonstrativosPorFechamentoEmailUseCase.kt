package br.com.edificiopromenade.domain.usecase.email

import br.com.edificiopromenade.domain.repository.DemonstrativoRepository
import br.com.edificiopromenade.presentation.common.mapper.toUi
import br.com.edificiopromenade.presentation.history.model.DemonstrativoHistoricoUi
import jakarta.inject.Inject

class ConsultarDemonstrativosPorFechamentoEmailUseCase @Inject constructor(
    private val repository: DemonstrativoRepository
) {
    suspend operator fun invoke(
        fechamentoId: Long
    ): List<DemonstrativoHistoricoUi> =
        repository
            .consultarPorFechamento(fechamentoId)
            .map {
                it.toUi()
            }
}