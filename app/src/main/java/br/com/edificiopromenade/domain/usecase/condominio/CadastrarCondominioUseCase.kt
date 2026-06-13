package br.com.edificiopromenade.domain.usecase.condominio

import br.com.edificiopromenade.data.local.entity.CondominioEntity
import br.com.edificiopromenade.domain.repository.CondominioRepository
import java.time.LocalDateTime
import jakarta.inject.Inject

class CadastrarCondominioUseCase @Inject constructor(
    private val repository: CondominioRepository
) {

    suspend operator fun invoke(
        condominio: CondominioEntity
    ): Long {

        val atual = repository.findAtivo()

        atual?.let {

            repository.update(

                it.copy(
                    ativo = false,
                    dataInativacao = LocalDateTime.now()
                )
            )
        }

        return repository.insert(
            condominio.copy(
                ativo = true
            )
        )
    }
}