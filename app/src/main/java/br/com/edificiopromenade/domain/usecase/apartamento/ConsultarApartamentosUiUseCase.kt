package br.com.edificiopromenade.domain.usecase.apartamento

import br.com.edificiopromenade.domain.repository.ApartamentoRepository
import br.com.edificiopromenade.presentation.apartment.mapper.toUi
import br.com.edificiopromenade.presentation.apartment.model.ApartamentoUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import jakarta.inject.Inject

class ConsultarApartamentosUiUseCase @Inject constructor(
    private val repository: ApartamentoRepository
) {

    operator fun invoke(): Flow<List<ApartamentoUi>> {

        return repository
            .findAll()
            .map { lista ->
                lista.map {
                    it.toUi()
                }
            }
    }
}