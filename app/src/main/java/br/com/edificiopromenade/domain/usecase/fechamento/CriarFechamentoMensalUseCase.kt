package br.com.edificiopromenade.domain.usecase.fechamento

import br.com.edificiopromenade.data.local.entity.FechamentoMensalEntity
import br.com.edificiopromenade.domain.repository.FechamentoRepository
import br.com.edificiopromenade.domain.usecase.despesatemporaria.AssociarParcelasAoFechamentoUseCase
import jakarta.inject.Inject

class CriarFechamentoMensalUseCase @Inject constructor(
    private val repository: FechamentoRepository,
    private val associarParcelasAoFechamentoUseCase: AssociarParcelasAoFechamentoUseCase
) {

    suspend operator fun invoke(
        fechamento: FechamentoMensalEntity
    ): Long {

        val fechamentoId = repository.insert(fechamento)

        val competencia = fechamento.ano * 100 + fechamento.mes

        associarParcelasAoFechamentoUseCase(
            competencia = competencia,
            fechamentoId = fechamentoId,
            )

        return fechamentoId
    }
}