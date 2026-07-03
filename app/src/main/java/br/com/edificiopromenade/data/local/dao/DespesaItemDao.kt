package br.com.edificiopromenade.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.edificiopromenade.data.local.entity.DespesaItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DespesaItemDao {

    @Insert
    suspend fun insert(
        item: DespesaItemEntity
    ): Long

    @Update
    suspend fun update(
        item: DespesaItemEntity
    )

    @Delete
    suspend fun delete(
        item: DespesaItemEntity
    )

    @Query("""
        SELECT *
        FROM despesa_itens
        WHERE despesaId = :despesaId
        ORDER BY descricao
    """)
    fun findByDespesa(
        despesaId: Long
    ): Flow<List<DespesaItemEntity>>

    @Query("""
        SELECT *
        FROM despesa_itens
        WHERE despesaId = :despesaId
        ORDER BY descricao
    """)
    suspend fun findListByDespesa(
        despesaId: Long
    ): List<DespesaItemEntity>

    @Query("""
        SELECT COALESCE(SUM(valor),0)
        FROM despesa_itens
        WHERE despesaId = :despesaId
    """)
    suspend fun calcularTotalItens(
        despesaId: Long
    ): Double
}