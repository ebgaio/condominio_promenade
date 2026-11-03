package br.com.edificiopromenade.domain.usecase.despesaitem

import br.com.edificiopromenade.data.local.entity.DespesaItemEntity
import br.com.edificiopromenade.domain.repository.DespesaItemRepository
import br.com.edificiopromenade.domain.repository.TransacaoRepository
import br.com.edificiopromenade.domain.usecase.despesa.AtualizarTotalDespesaUseCase
import jakarta.inject.Inject

class AtualizarDespesaItemUseCase @Inject constructor(
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
             * 1. LOCALIZA O ITEM ORIGINAL
             * =====================================================
             */
            val itemAtual = repository.findById(item.id)
                    ?: throw IllegalStateException(
                        "Item de despesa não encontrado."
                    )

            /*
             * =====================================================
             * 2. PROTEGE O HISTÓRICO
             * =====================================================
             */
            validarDespesaItemMutavelUseCase(itemAtual.despesaId)

            /*
             * =====================================================
             * 3. NÃO PERMITE TROCAR A DESPESA DO ITEM
             * =====================================================
             */
            if (
                item.despesaId != itemAtual.despesaId
            ) {

                throw IllegalStateException(
                    "Não é permitido transferir um item para outra despesa."
                )
            }

            /*
             * =====================================================
             * 4. ATUALIZA O ITEM
             * =====================================================
             */
            repository.update(item)

            /*
             * =====================================================
             * 5. RECALCULA O TOTAL
             * =====================================================
             *
             * Falhou aqui?
             *
             * O UPDATE anterior também sofre rollback.
             */
            atualizarTotalDespesaUseCase(itemAtual.despesaId)
        }
    }
}