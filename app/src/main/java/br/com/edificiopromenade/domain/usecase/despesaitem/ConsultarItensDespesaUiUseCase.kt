package br.com.edificiopromenade.domain.usecase.despesaitem

import br.com.edificiopromenade.presentation.despesaitem.mapper.toUi
import jakarta.inject.Inject
import kotlinx.coroutines.flow.map

class ConsultarItensDespesaUiUseCase @Inject constructor(
    private val consultarItensDespesaUseCase: ConsultarItensDespesaUseCase
) {
    operator fun invoke(
        despesaId: Long
    ) = consultarItensDespesaUseCase(
            despesaId
        ).map { lista ->
            lista.map {
                it.toUi()
            }
        }
}