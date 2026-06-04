package br.com.edificiopromenade.domain.usecase.condominio

import br.com.edificiopromenade.domain.repository.CondominioRepository
import javax.inject.Inject

class ConsultarHistoricoCondominioUseCase @Inject constructor(
    private val repository: CondominioRepository
) {

    operator fun invoke() =
        repository.findHistorico()
}