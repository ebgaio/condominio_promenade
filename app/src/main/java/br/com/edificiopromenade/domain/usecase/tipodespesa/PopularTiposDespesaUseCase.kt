package br.com.edificiopromenade.domain.usecase.tipodespesa

import br.com.edificiopromenade.data.local.entity.TipoDespesaEntity
import br.com.edificiopromenade.domain.repository.TipoDespesaRepository
import kotlinx.coroutines.flow.first
import jakarta.inject.Inject

class PopularTiposDespesaUseCase @Inject constructor(
    private val repository: TipoDespesaRepository
) {

    suspend operator fun invoke() {

        val tiposPadrao = listOf(
            "Energia",
            "Limpeza",
            "Salários",
            "Encargos Sociais",
            "Honorários Síndico",
            "Tributos Federais",
            "COPASA",
            "Despesa Avulsa"
        )

        tiposPadrao.forEach { descricao ->

            val existente =
                repository.findAtivoByDescricao(
                    descricao
                )

            if (existente == null) {

                repository.insert(
                    TipoDespesaEntity(
                        descricao = descricao,
                        recorrente = false,
                        usaFracaoIdeal = descricao == "COPASA"
                    )
                )
            }
        }
    }
}