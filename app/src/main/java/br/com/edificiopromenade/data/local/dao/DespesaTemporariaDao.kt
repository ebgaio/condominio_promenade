package br.com.edificiopromenade.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.edificiopromenade.data.local.entity.DespesaTemporariaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DespesaTemporariaDao {

    @Insert
    suspend fun insert(
        despesa: DespesaTemporariaEntity
    ): Long

    @Update
    suspend fun update(
        despesa: DespesaTemporariaEntity
    )

    @Query("""
        SELECT *
        FROM despesas_temporarias
        WHERE ativa = 1
        ORDER BY descricao
    """)
    fun findAtivas(): Flow<List<DespesaTemporariaEntity>>

    @Query("""
        SELECT *
        FROM despesas_temporarias
        WHERE id = :id
    """)
    suspend fun findById(
        id: Long
    ): DespesaTemporariaEntity?

    @Query("""
        UPDATE despesas_temporarias
        SET ativa = 0
        WHERE id = :id
    """)
    suspend fun encerrarDespesaTemporaria(
        id: Long
    )
}