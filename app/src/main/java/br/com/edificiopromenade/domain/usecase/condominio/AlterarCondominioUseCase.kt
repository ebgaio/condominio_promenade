package br.com.edificiopromenade.domain.usecase.condominio

import br.com.edificiopromenade.data.local.entity.CondominioEntity
import br.com.edificiopromenade.domain.repository.CondominioRepository
import java.time.LocalDateTime
import javax.inject.Inject

class AlterarCondominioUseCase @Inject constructor(
    private val repository: CondominioRepository
) {

    suspend operator fun invoke(
        condominioAtual: CondominioEntity,
        novoCondominio: CondominioEntity
    ) {

        repository.update(
            condominioAtual.copy(
                ativo = false,
                dataInativacao = LocalDateTime.now()
            )
        )

        repository.insert(
            novoCondominio.copy(
                id = 0,
                ativo = true,
                dataCriacao = LocalDateTime.now(),
                dataInativacao = null
            )
        )
    }
}