package br.com.edificiopromenade.data.local.dao

import androidx.room.*
import br.com.edificiopromenade.data.local.entity.MoradorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MoradorDao {

    @Insert
    suspend fun insert(
        morador: MoradorEntity
    ): Long

    @Update
    suspend fun update(
        morador: MoradorEntity
    )

    @Query("""
        SELECT *
        FROM moradores
        WHERE ativo = 1
        ORDER BY nome
    """)
    fun findAllAtivos(): Flow<List<MoradorEntity>>

    @Query("""
        SELECT *
        FROM moradores
        WHERE apartamentoId = :apartamentoId
        AND ativo = 1
        LIMIT 1
    """)
    suspend fun findMoradorAtual(
        apartamentoId: Long
    ): MoradorEntity?
}