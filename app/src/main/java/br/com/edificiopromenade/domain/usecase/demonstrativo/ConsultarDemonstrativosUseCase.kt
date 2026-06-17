package br.com.edificiopromenade.domain.usecase.demonstrativo

import br.com.edificiopromenade.domain.repository.DemonstrativoRepository
import jakarta.inject.Inject

class ConsultarDemonstrativosUseCase @Inject constructor(
    private val repository: DemonstrativoRepository
) {

    suspend operator fun invoke(
        fechamentoId: Long
    ) =
        repository.consultarPorFechamento(
            fechamentoId
        )
}