package br.com.edificiopromenade.domain.usecase.despesa

import br.com.edificiopromenade.domain.model.ExcluirDespesaCommand
import br.com.edificiopromenade.domain.repository.ExclusaoDespesaRepository
import jakarta.inject.Inject

class ExcluirDespesaUseCase @Inject constructor(
    private val repository: ExclusaoDespesaRepository
) {

    suspend operator fun invoke(
        command: ExcluirDespesaCommand
    ) {

        repository.excluirRascunho(
            command.despesaId
        )
    }
}