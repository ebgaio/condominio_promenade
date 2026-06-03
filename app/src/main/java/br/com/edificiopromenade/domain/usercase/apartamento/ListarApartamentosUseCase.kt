package br.com.edificiopromenade.domain.usercase.apartamento

import br.com.edificiopromenade.domain.repository.ApartamentoRepository
import javax.inject.Inject

class ListarApartamentosUseCase @Inject constructor(
    private val repository: ApartamentoRepository
) {

    operator fun invoke() =
        repository.findAll()
}