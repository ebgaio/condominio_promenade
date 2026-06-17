package br.com.edificiopromenade.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.edificiopromenade.data.local.entity.DespesaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DespesaDao {

    @Insert
    suspend fun insert(
        despesa: DespesaEntity
    ): Long

    @Update
    suspend fun update(
        despesa: DespesaEntity
    )

    @Delete
    suspend fun delete(
        despesa: DespesaEntity
    )

    @Query("""
        SELECT *
        FROM despesas
        WHERE fechamentoId = :fechamentoId
        ORDER BY descricao
    """)
    fun findByFechamento(
        fechamentoId: Long
    ): Flow<List<DespesaEntity>>

    @Query("""
        SELECT *
        FROM despesas
        WHERE fechamentoId = :fechamentoId
        ORDER BY descricao
    """)
    suspend fun findListByFechamento(
        fechamentoId: Long
    ): List<DespesaEntity>

    @Query("""
        SELECT COUNT(*)
        FROM despesas
        WHERE fechamentoId = :fechamentoId
        AND descricao = :descricao
    """)
    suspend fun countByDescricao(
        fechamentoId: Long,
        descricao: String
    ): Int
}