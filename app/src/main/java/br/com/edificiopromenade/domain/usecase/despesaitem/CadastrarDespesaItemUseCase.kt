package br.com.edificiopromenade.domain.usecase.despesaitem

import br.com.edificiopromenade.data.local.entity.DespesaItemEntity
import br.com.edificiopromenade.domain.repository.DespesaItemRepository
import br.com.edificiopromenade.domain.repository.TransacaoRepository
import br.com.edificiopromenade.domain.usecase.despesa.AtualizarTotalDespesaUseCase
import jakarta.inject.Inject

class CadastrarDespesaItemUseCase @Inject constructor(
    private val repository: DespesaItemRepository,
    private val atualizarTotalDespesaUseCase: AtualizarTotalDespesaUseCase,
    private val validarDespesaItemMutavelUseCase: ValidarDespesaItemMutavelUseCase,
    private val transacaoRepository: TransacaoRepository
) {
    suspend operator fun invoke(
        item: DespesaItemEntity
    ): Long {

        return transacaoRepository.executar {

            /*
             * =====================================================
             * 1. PROTEGE FECHAMENTO FINALIZADO
             * =====================================================
             *
             * A validação fica DENTRO da mesma transação.
             */
            validarDespesaItemMutavelUseCase(item.despesaId)

            /*
             * =====================================================
             * 2. INSERE O ITEM
             * =====================================================
             */
            val id = repository.insert(item)

            /*
             * =====================================================
             * 3. RECALCULA O TOTAL
             * =====================================================
             *
             * Se esta etapa falhar, o INSERT também
             * será revertido.
             */
            atualizarTotalDespesaUseCase(item.despesaId)

            id
        }
    }
}