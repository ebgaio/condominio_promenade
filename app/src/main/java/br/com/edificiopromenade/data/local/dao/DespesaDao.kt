package br.com.edificiopromenade.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import br.com.edificiopromenade.data.local.entity.DespesaComItensEntity
import br.com.edificiopromenade.data.local.entity.DespesaComTipoEntity
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
        DELETE FROM despesas
        WHERE id = :id
    """)
    suspend fun deleteById(
        id: Long
    )

    @Query("""
        SELECT *
        FROM despesas
        WHERE fechamentoId = :fechamentoId
        ORDER BY descricaoLivre
    """)
    fun findByFechamento(
        fechamentoId: Long
    ): Flow<List<DespesaEntity>>

    @Query("""
        SELECT *
        FROM despesas
        WHERE fechamentoId = :fechamentoId
        ORDER BY descricaoLivre
    """)
    suspend fun findListByFechamento(
        fechamentoId: Long
    ): List<DespesaEntity>

    @Query("""
        SELECT COUNT(*)
        FROM despesas
        WHERE fechamentoId = :fechamentoId
        AND tipoDespesaId = :tipoDespesaId
    """)
    suspend fun countByDescricao(
        fechamentoId: Long,
        tipoDespesaId: Long
    ): Int

    @Transaction
    @Query("""
        SELECT *
        FROM despesas
        WHERE fechamentoId = :fechamentoId
        ORDER BY descricaoLivre
    """)
    suspend fun findComTipoByFechamento(
        fechamentoId: Long
    ): List<DespesaComTipoEntity>

    @Transaction
    @Query("""
        SELECT *
        FROM despesas
        WHERE fechamentoId = :fechamentoId
        ORDER BY descricaoLivre
    """)
    fun findComTipoByFechamentoFlow(
        fechamentoId: Long
    ): Flow<List<DespesaComTipoEntity>>

    @Query("""
        SELECT *
        FROM despesas
        WHERE id = :id
    """)
    suspend fun findById(
        id: Long
    ): DespesaEntity?

    @Transaction
    @Query("""
        SELECT *
        FROM despesas
        WHERE id = :despesaId
    """)
    suspend fun findComItens(
        despesaId: Long
    ): DespesaComItensEntity?

    @Transaction
    @Query("""
        SELECT *
        FROM despesas
        WHERE fechamentoId = :fechamentoId
        ORDER BY descricaoLivre
    """)
    fun findComItensByFechamento(
        fechamentoId: Long
    ): Flow<List<DespesaComItensEntity>>

    @Transaction
    @Query("""
        SELECT *
        FROM despesas
        WHERE fechamentoId = :fechamentoId
        ORDER BY descricaoLivre
    """)
    suspend fun findComItensListByFechamento(
        fechamentoId: Long
    ): List<DespesaComItensEntity>
}