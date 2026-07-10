package br.com.edificiopromenade.domain.usecase.morador

import br.com.edificiopromenade.domain.repository.ApartamentoRepository
import br.com.edificiopromenade.presentation.apartment.detail.mapper.toUi
import jakarta.inject.Inject
import kotlinx.coroutines.flow.map

class ListarApartamentosComMoradoresUiUseCase @Inject constructor(
    private val repository: ApartamentoRepository
) {
    operator fun invoke() =

        repository
            .findAllComMoradores()
            .map { lista ->
                lista.map {
                    it.toUi()
                }
            }
}