package br.com.edificiopromenade.presentation.tipodespesa.usecase

import br.com.edificiopromenade.domain.usecase.tipodespesa.ConsultarTiposDespesaUseCase
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