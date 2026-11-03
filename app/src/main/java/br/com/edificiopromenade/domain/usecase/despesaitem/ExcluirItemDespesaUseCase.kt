package br.com.edificiopromenade.domain.usecase.despesaitem

import br.com.edificiopromenade.data.local.entity.DespesaItemEntity
import br.com.edificiopromenade.domain.repository.DespesaItemRepository
import br.com.edificiopromenade.domain.repository.TransacaoRepository
import br.com.edificiopromenade.domain.usecase.despesa.AtualizarTotalDespesaUseCase
import jakarta.inject.Inject

class ExcluirItemDespesaUseCase @Inject constructor(
    private val repository: DespesaItemRepository,
    private val atualizarTotalDespesaUseCase: AtualizarTotalDespesaUseCase,
    private val validarDespesaItemMutavelUseCase: ValidarDespesaItemMutavelUseCase,
    private val transacaoRepository: TransacaoRepository
) {
    suspend operator fun invoke(
        item: DespesaItemEntity
    ) {
        transacaoRepository.executar {

            /*
         * =====================================================
         * 1. LOCALIZA O ITEM PERSISTIDO
         * =====================================================
         */
            val itemAtual = repository.findById(item.id)
                ?: throw IllegalStateException(
                    "Item de despesa não encontrado."
                )

            /*
         * =====================================================
         * 2. PROTEGE FECHAMENTO FINALIZADO
         * =====================================================
         */
            validarDespesaItemMutavelUseCase(itemAtual.despesaId)

            /*
         * =====================================================
         * 3. EXCLUI O ITEM
         * =====================================================
         */
            repository.delete(itemAtual)

            /*
         * =====================================================
         * 4. RECALCULA O TOTAL
         * =====================================================
         *
         * Se falhar, o DELETE também é revertido.
         */
            atualizarTotalDespesaUseCase(itemAtual.despesaId)
        }
    }
}