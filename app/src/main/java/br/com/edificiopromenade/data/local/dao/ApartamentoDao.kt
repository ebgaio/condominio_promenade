package br.com.edificiopromenade.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import br.com.edificiopromenade.data.local.entity.ApartamentoEntity
import br.com.edificiopromenade.data.local.relation.ApartamentoComMorador
import br.com.edificiopromenade.data.local.relation.ApartamentoDetalhado
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
        WHERE ativo = 1
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
        WHERE ativo = 1
        ORDER BY numero
    """)
    fun findAllComMoradores():
            Flow<List<ApartamentoComMorador>>

    @Transaction
    @Query("""
        SELECT *
        FROM apartamentos
        WHERE id = :id
    """)
    suspend fun findDetalhado(
        id: Long
    ): ApartamentoDetalhado?

    @Query("""
        UPDATE apartamentos
        SET ativo = 0
        WHERE id = :id
    """)
    suspend fun inativar(
        id: Long
    )

    @Query("""
        SELECT *
        FROM apartamentos
        WHERE ativo = 1
        ORDER BY numero
    """)
    suspend fun findAllAtivosList():
            List<ApartamentoEntity>
}