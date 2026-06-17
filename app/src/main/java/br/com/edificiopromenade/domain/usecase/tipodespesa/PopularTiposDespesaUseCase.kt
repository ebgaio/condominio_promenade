package br.com.edificiopromenade.domain.usecase.tipodespesa

import br.com.edificiopromenade.data.local.entity.TipoDespesaEntity
import kotlinx.coroutines.flow.first
import jakarta.inject.Inject

class PopularTiposDespesaUseCase @Inject constructor(

    private val consultarTiposDespesaUseCase: ConsultarTiposDespesaUseCase,
    private val cadastrarTipoDespesaUseCase: CadastrarTipoDespesaUseCase
) {

    suspend operator fun invoke() {

        val existentes = consultarTiposDespesaUseCase().first()

        if (existentes.isNotEmpty())
            return

        listOf(
            "Energia",
            "Limpeza",
            "Salários",
            "Encargos Sociais",
            "Honorários Síndico",
            "Tributos Federais",
            "COPASA"

        ).forEach { descricao ->
            cadastrarTipoDespesaUseCase(
                TipoDespesaEntity(
                    descricao = descricao,
                    recorrente = false
                )
            )
        }
    }
}