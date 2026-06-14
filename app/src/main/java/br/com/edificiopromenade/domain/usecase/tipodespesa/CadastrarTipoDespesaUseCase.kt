package br.com.edificiopromenade.domain.usecase.tipodespesa

import br.com.edificiopromenade.data.local.entity.TipoDespesaEntity
import br.com.edificiopromenade.domain.repository.TipoDespesaRepository
import javax.inject.Inject

class CadastrarTipoDespesaUseCase @Inject constructor(
    private val repository: TipoDespesaRepository
) {

    suspend operator fun invoke(
        tipo: TipoDespesaEntity
    ): Long {

        return repository.insert(tipo)
    }
}