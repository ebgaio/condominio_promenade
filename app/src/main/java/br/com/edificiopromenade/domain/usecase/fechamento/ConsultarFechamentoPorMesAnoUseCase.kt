package br.com.edificiopromenade.domain.usecase.fechamento

import br.com.edificiopromenade.domain.repository.FechamentoRepository
import jakarta.inject.Inject

class ConsultarFechamentoPorMesAnoUseCase @Inject constructor(
    private val repository: FechamentoRepository
) {

    suspend operator fun invoke(
        mes: Int,
        ano: Int
    ) = repository.findByMesAno(
            mes,
            ano
        )
}