package br.com.edificiopromenade.domain.usecase.tipodespesa

import br.com.edificiopromenade.domain.repository.TipoDespesaRepository
import javax.inject.Inject

class ConsultarTiposDespesaUseCase @Inject constructor(
    private val repository: TipoDespesaRepository
) {

    operator fun invoke() =
        repository.findAllAtivos()
}