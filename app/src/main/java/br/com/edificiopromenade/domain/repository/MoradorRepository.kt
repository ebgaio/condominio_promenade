package br.com.edificiopromenade.domain.repository

import br.com.edificiopromenade.data.local.entity.ApartamentoEntity
import br.com.edificiopromenade.data.local.entity.MoradorEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface MoradorRepository {

    fun findAllAtivos(): Flow<List<MoradorEntity>>

    fun findAll(): Flow<List<MoradorEntity>>

    suspend fun findById(
        id: Long
    ): MoradorEntity?

    suspend fun insert(
        morador: MoradorEntity
    ): Long

    suspend fun update(
        morador: MoradorEntity
    )

    suspend fun encerrarMorador(
        id: Long,
        dataFim: LocalDate
    )

    suspend fun findHistoricoPorApartamento(
        apartamentoId: Long
    ): List<MoradorEntity>

    suspend fun findMoradorAtivoPorApartamento(
        apartamentoId: Long
    ): MoradorEntity?

    suspend fun findAllAtivosList():
            List<ApartamentoEntity>
}