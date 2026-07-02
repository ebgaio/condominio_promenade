package br.com.edificiopromenade.domain.usecase.despesa

import br.com.edificiopromenade.data.local.entity.DespesaEntity
import br.com.edificiopromenade.domain.model.CadastrarDespesaCommand
import br.com.edificiopromenade.domain.repository.DespesaRepository
import jakarta.inject.Inject

class CadastrarDespesaUseCase @Inject constructor(
    private val repository: DespesaRepository
) {

    suspend operator fun invoke(
        command: CadastrarDespesaCommand
    ): Long {

        val entity = DespesaEntity(
            fechamentoId = command.fechamentoId,
            tipoDespesaId = command.tipoDespesaId,
            descricaoLivre = command.descricaoLivre,
            valor = command.valor
        )
        return repository.insert(entity)
    }
}