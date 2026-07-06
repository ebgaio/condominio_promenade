package br.com.edificiopromenade.domain.usecase.tipodespesa

import br.com.edificiopromenade.presentation.tipodespesa.mapper.toUi
import jakarta.inject.Inject
import kotlinx.coroutines.flow.map

class ConsultarTiposDespesaUiUseCase @Inject constructor(
    private val consultarTiposDespesaUseCase: ConsultarTiposDespesaUseCase
) {
    operator fun invoke() =

        consultarTiposDespesaUseCase()
            .map { lista ->
                lista.map { tipo ->
                    tipo.toUi()
                }
            }
}