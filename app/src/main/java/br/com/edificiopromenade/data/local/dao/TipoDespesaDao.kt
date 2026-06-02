package br.com.edificiopromenade.data.local.dao

import androidx.room.*
import br.com.edificiopromenade.data.local.entity.TipoDespesaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TipoDespesaDao {

    @Insert
    suspend fun insert(
        tipo: TipoDespesaEntity
    ): Long

    @Update
    suspend fun update(
        tipo: TipoDespesaEntity
    )

    @Query("""
        SELECT *
        FROM tipos_despesa
        WHERE ativo = 1
        ORDER BY descricao
    """)
    fun findAllAtivos(): Flow<List<TipoDespesaEntity>>

    @Query("""
        SELECT *
        FROM tipos_despesa
        WHERE id = :id
    """)
    suspend fun findById(
        id: Long
    ): TipoDespesaEntity?
}