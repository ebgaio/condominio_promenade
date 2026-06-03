package br.com.edificiopromenade.domain.usecase.fechamento

import br.com.edificiopromenade.domain.repository.FechamentoRepository
import javax.inject.Inject

class ConsultarFechamentosUseCase @Inject constructor(
    private val repository: FechamentoRepository
) {

    operator fun invoke() =
        repository.findAll()
}