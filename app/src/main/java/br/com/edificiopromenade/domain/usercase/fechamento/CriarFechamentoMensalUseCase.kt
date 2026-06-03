package br.com.edificiopromenade.domain.usercase.fechamento

import br.com.edificiopromenade.data.local.entity.FechamentoMensalEntity
import br.com.edificiopromenade.domain.repository.FechamentoRepository
import javax.inject.Inject

class CriarFechamentoMensalUseCase @Inject constructor(
    private val repository: FechamentoRepository
) {

    suspend operator fun invoke(
        fechamento: FechamentoMensalEntity
    ): Long {

        return repository.insert(fechamento)
    }
}