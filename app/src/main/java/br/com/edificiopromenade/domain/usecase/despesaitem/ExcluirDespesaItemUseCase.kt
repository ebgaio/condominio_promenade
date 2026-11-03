package br.com.edificiopromenade.domain.usecase.despesaitem

import br.com.edificiopromenade.data.local.entity.DespesaItemEntity
import br.com.edificiopromenade.domain.repository.DespesaItemRepository
import br.com.edificiopromenade.domain.usecase.despesa.AtualizarTotalDespesaUseCase
import jakarta.inject.Inject

class ExcluirDespesaItemUseCase @Inject constructor(
    private val repository: DespesaItemRepository,
    private val atualizarTotalDespesaUseCase: AtualizarTotalDespesaUseCase,
    private val validarDespesaItemMutavelUseCase: ValidarDespesaItemMutavelUseCase
) {
    suspend operator fun invoke(
        item: DespesaItemEntity
    ) {

        /*
         * =====================================================
         * 1. LOCALIZA O ITEM REAL
         * =====================================================
         */
        val itemAtual = repository.findById(item.id)
            ?: throw IllegalStateException(
                "Item de despesa não encontrado."
            )

        /*
         * =====================================================
         * 2. PROTEGE O FECHAMENTO HISTÓRICO
         * =====================================================
         */
        validarDespesaItemMutavelUseCase(itemAtual.despesaId)

        /*
         * =====================================================
         * 3. EXCLUI O REGISTRO PERSISTIDO
         * =====================================================
         *
         * Utilizamos itemAtual em vez do objeto
         * recebido externamente.
         */
        repository.delete(itemAtual)

        /*
         * =====================================================
         * 4. RECALCULA O TOTAL
         * =====================================================
         */
        atualizarTotalDespesaUseCase(itemAtual.despesaId)
    }
}