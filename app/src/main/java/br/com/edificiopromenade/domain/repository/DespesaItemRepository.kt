package br.com.edificiopromenade.domain.repository

import br.com.edificiopromenade.data.local.entity.DespesaItemEntity
import kotlinx.coroutines.flow.Flow

interface DespesaItemRepository {

    suspend fun insert(
        item: DespesaItemEntity
    ): Long

    suspend fun update(
        item: DespesaItemEntity
    )

    suspend fun delete(
        item: DespesaItemEntity
    )

    fun findByDespesa(
        despesaId: Long
    ): Flow<List<DespesaItemEntity>>

    suspend fun findListByDespesa(
        despesaId: Long
    ): List<DespesaItemEntity>

    suspend fun calcularTotalItens(
        despesaId: Long
    ): Double
}