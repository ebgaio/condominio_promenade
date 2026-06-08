package br.com.edificiopromenade.domain.repository

import br.com.edificiopromenade.data.local.entity.MoradorEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface MoradorRepository {

    fun findAllAtivos(): Flow<List<MoradorEntity>>

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
}