package br.com.edificiopromenade.domain.usecase.fechamento

import br.com.edificiopromenade.data.local.entity.FechamentoMensalEntity
import br.com.edificiopromenade.domain.repository.FechamentoRepository
import jakarta.inject.Inject

class ConsultarFechamentoPorIdUseCase @Inject constructor(
    private val repository: FechamentoRepository
) {

    suspend operator fun invoke(
        fechamentoId: Long
    ): FechamentoMensalEntity? {

        return repository.findById(
            fechamentoId
        )
    }
}