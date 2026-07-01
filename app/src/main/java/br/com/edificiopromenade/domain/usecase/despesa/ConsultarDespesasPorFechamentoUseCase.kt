package br.com.edificiopromenade.domain.usecase.despesa

import br.com.edificiopromenade.domain.repository.DespesaRepository
import jakarta.inject.Inject

class ConsultarDespesasPorFechamentoUseCase @Inject constructor(
    private val repository: DespesaRepository
) {

    operator fun invoke(
        fechamentoId: Long
    ) =
        repository.findComTipoByFechamentoFlow(
            fechamentoId
        )
}