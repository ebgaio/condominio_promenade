package br.com.edificiopromenade.data.local.dao

import androidx.room.*
import br.com.edificiopromenade.data.local.entity.ApartamentoEntity
import br.com.edificiopromenade.data.local.entity.MoradorEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

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
        WHERE id = :id
    """)
    suspend fun findById(
        id: Long
    ): MoradorEntity?

    @Query("""
        UPDATE moradores
        SET ativo = 0,
            dataFim = :dataFim
        WHERE id = :id
    """)
    suspend fun encerrarMorador(
        id: Long,
        dataFim: LocalDate
    )

    @Query("""
        SELECT *
        FROM moradores
        WHERE apartamentoId = :apartamentoId
        ORDER BY dataInicio DESC
    """)
    suspend fun findHistoricoPorApartamento(
        apartamentoId: Long
    ): List<MoradorEntity>

    @Query("""
        SELECT *
        FROM moradores
        WHERE apartamentoId = :apartamentoId
        AND ativo = 1
        LIMIT 1
    """)
    suspend fun findMoradorAtivoPorApartamento(
        apartamentoId: Long
    ): MoradorEntity?

    @Query("""
        SELECT *
        FROM apartamentos
        WHERE ativo = 1
        ORDER BY numero
    """)
    suspend fun findAllAtivosList():
            List<ApartamentoEntity>
}