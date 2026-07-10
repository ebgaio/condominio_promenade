package br.com.edificiopromenade.domain.usecase.morador

import br.com.edificiopromenade.domain.repository.MoradorRepository
import br.com.edificiopromenade.presentation.resident.mapper.toUi
import br.com.edificiopromenade.presentation.resident.model.MoradorUi
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ConsultarMoradoresUiUseCase @Inject constructor(
    private val repository: MoradorRepository
) {
    operator fun invoke(): Flow<List<MoradorUi>> {

        return repository
            .findAll()
            .map { lista ->
                lista.map { it.toUi() }
            }
    }
}