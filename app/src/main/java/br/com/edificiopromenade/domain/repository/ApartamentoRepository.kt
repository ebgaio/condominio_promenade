package br.com.edificiopromenade.domain.repository

import br.com.edificiopromenade.data.local.entity.ApartamentoEntity
import br.com.edificiopromenade.data.local.relation.ApartamentoComMorador
import kotlinx.coroutines.flow.Flow

interface ApartamentoRepository {

    fun findAll(): Flow<List<ApartamentoEntity>>

    fun findAllComMoradores():
            Flow<List<ApartamentoComMorador>>

    suspend fun findById(
        id: Long
    ): ApartamentoEntity?

    suspend fun insert(
        apartamento: ApartamentoEntity
    ): Long

    suspend fun update(
        apartamento: ApartamentoEntity
    )
}