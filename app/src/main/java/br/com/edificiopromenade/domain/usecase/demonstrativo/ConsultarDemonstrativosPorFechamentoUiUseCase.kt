package br.com.edificiopromenade.domain.usecase.demonstrativo

import br.com.edificiopromenade.domain.repository.DemonstrativoRepository
import br.com.edificiopromenade.presentation.demonstrativo.mapper.toUi
import br.com.edificiopromenade.presentation.demonstrativo.model.DemonstrativoUi
import jakarta.inject.Inject

class ConsultarDemonstrativosPorFechamentoUiUseCase @Inject constructor(
    private val repository: DemonstrativoRepository
) {
    suspend operator fun invoke(
        fechamentoId: Long
    ): List<DemonstrativoUi> =
        repository
            .consultarPorFechamento(fechamentoId)
            .map {
                it.toUi()
            }
}