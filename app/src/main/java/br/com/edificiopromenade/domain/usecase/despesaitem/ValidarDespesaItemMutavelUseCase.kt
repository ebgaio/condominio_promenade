package br.com.edificiopromenade.domain.usecase.despesaitem

import br.com.edificiopromenade.domain.repository.DespesaRepository
import br.com.edificiopromenade.domain.repository.FechamentoRepository
import jakarta.inject.Inject

class ValidarDespesaItemMutavelUseCase @Inject constructor(

    private val despesaRepository: DespesaRepository,
    private val fechamentoRepository: FechamentoRepository

) {
    suspend operator fun invoke(
        despesaId: Long
    ) {

        /*
         * =====================================================
         * 1. LOCALIZA A DESPESA
         * =====================================================
         */
        val despesa = despesaRepository.findById(
                despesaId
            )
                ?: throw IllegalStateException("Despesa não encontrada.")

        /*
         * =====================================================
         * 2. LOCALIZA O FECHAMENTO
         * =====================================================
         */
        val fechamento = fechamentoRepository.findById(
                despesa.fechamentoId
            )
                ?: throw IllegalStateException("Fechamento da despesa não encontrado.")

        /*
         * =====================================================
         * 3. PROTEGE O HISTÓRICO
         * =====================================================
         */
        if (fechamento.finalizado) {

            throw IllegalStateException(
                "Não é permitido alterar itens de uma despesa de um fechamento finalizado."
            )
        }
    }
}