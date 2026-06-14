package br.com.edificiopromenade.domain.usecase.fechamento

import br.com.edificiopromenade.domain.model.DemonstrativoCalculado
import javax.inject.Inject

class CalcularRateioMensalUseCase @Inject constructor() {

    suspend operator fun invoke() : List<DemonstrativoCalculado> {

        return emptyList()
    }
}