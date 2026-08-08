package br.com.edificiopromenade.domain.usecase.despesatemporaria

import br.com.edificiopromenade.data.local.entity.DespesaTemporariaParcelaEntity
import br.com.edificiopromenade.domain.repository.DespesaTemporariaParcelaRepository
import jakarta.inject.Inject

class CadastrarDespesaTemporariaParcelaUseCase @Inject constructor(
    private val repository: DespesaTemporariaParcelaRepository
) {

    suspend operator fun invoke(
        parcela: DespesaTemporariaParcelaEntity
    ): Long {

        require(
            parcela.despesaTemporariaId > 0
        ) {
            "Despesa temporária inválida."
        }

        require(
            parcela.numeroParcela > 0
        ) {
            "Número da parcela inválido."
        }

        require(
            parcela.competencia > 0
        ) {
            "Competência inválida."
        }

        require(
            parcela.valor > 0.0
        ) {
            "O valor da parcela deve ser maior que zero."
        }

        return repository.insert(
            parcela
        )
    }
}