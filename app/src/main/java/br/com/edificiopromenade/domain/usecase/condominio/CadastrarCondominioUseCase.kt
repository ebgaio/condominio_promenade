package br.com.edificiopromenade.domain.usecase.condominio

import br.com.edificiopromenade.data.local.entity.CondominioEntity
import br.com.edificiopromenade.domain.repository.CondominioRepository
import jakarta.inject.Inject

class CadastrarCondominioUseCase @Inject constructor(
    private val repository: CondominioRepository
) {

    suspend operator fun invoke(
        condominio: CondominioEntity
    ): Long {

        val atual = repository.findAtivo()

        return if (atual != null) {

            val atualizado = condominio.copy(
                id = atual.id,
                ativo = true,
                dataCriacao = atual.dataCriacao,
                dataInativacao = null
            )

            repository.update(atualizado)

            atual.id
        } else {

            repository.insert(
                condominio.copy(
                    ativo = true
                )
            )
        }
    }
}