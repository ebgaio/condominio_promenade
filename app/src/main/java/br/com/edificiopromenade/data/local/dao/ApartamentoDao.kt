package br.com.edificiopromenade.data.local.dao

import androidx.room.*
import br.com.edificiopromenade.data.local.entity.ApartamentoEntity
import br.com.edificiopromenade.data.local.relation.ApartamentoComMorador
import kotlinx.coroutines.flow.Flow

@Dao
interface ApartamentoDao {

    @Insert
    suspend fun insert(
        apartamento: ApartamentoEntity
    ): Long

    @Update
    suspend fun update(
        apartamento: ApartamentoEntity
    )

    @Delete
    suspend fun delete(
        apartamento: ApartamentoEntity
    )

    @Query("""
        SELECT *
        FROM apartamentos
        ORDER BY numero
    """)
    fun findAll(): Flow<List<ApartamentoEntity>>

    @Query("""
        SELECT *
        FROM apartamentos
        WHERE id = :id
    """)
    suspend fun findById(
        id: Long
    ): ApartamentoEntity?

    @Transaction
    @Query("""
        SELECT *
        FROM apartamentos
        ORDER BY numero
    """)
    fun findAllComMoradores():
            Flow<List<ApartamentoComMorador>>
}