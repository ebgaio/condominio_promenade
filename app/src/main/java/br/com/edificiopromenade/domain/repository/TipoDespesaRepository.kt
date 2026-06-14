package br.com.edificiopromenade.domain.repository

import br.com.edificiopromenade.data.local.entity.TipoDespesaEntity
import kotlinx.coroutines.flow.Flow

interface TipoDespesaRepository {

    fun findAllAtivos():
        Flow<List<TipoDespesaEntity>>

    suspend fun findById(
        id: Long
    ): TipoDespesaEntity?

    suspend fun insert(
        tipo: TipoDespesaEntity
    ): Long

    suspend fun update(
        tipo: TipoDespesaEntity
    )
}