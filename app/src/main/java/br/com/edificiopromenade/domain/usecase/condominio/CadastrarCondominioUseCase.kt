package br.com.edificiopromenade.domain.usecase.condominio

import br.com.edificiopromenade.data.local.entity.CondominioEntity
import br.com.edificiopromenade.domain.repository.CondominioRepository
import javax.inject.Inject

class CadastrarCondominioUseCase @Inject constructor(
    private val repository: CondominioRepository
) {

    suspend operator fun invoke(
        condominio: CondominioEntity
    ): Long {

        return repository.insert(
            condominio
        )
    }
}