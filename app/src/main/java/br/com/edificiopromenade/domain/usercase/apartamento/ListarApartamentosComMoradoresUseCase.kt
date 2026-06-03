package br.com.edificiopromenade.domain.usercase.apartamento

import br.com.edificiopromenade.domain.repository.ApartamentoRepository
import javax.inject.Inject

class ListarApartamentosComMoradoresUseCase @Inject constructor(
    private val repository: ApartamentoRepository
) {

    operator fun invoke() =
        repository.findAllComMoradores()
}