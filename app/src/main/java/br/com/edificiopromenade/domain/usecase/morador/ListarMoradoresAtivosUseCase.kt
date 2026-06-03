package br.com.edificiopromenade.domain.usecase.morador

import br.com.edificiopromenade.domain.repository.MoradorRepository
import javax.inject.Inject

class ListarMoradoresAtivosUseCase @Inject constructor(
    private val repository: MoradorRepository
) {

    operator fun invoke() =
        repository.findAllAtivos()
}