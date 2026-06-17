package br.com.edificiopromenade.domain.repository

import br.com.edificiopromenade.data.local.entity.DespesaEntity
import kotlinx.coroutines.flow.Flow

interface DespesaRepository {

    fun findByFechamento(
        fechamentoId: Long
    ): Flow<List<DespesaEntity>>

    suspend fun insert(
        despesa: DespesaEntity
    ): Long

    suspend fun update(
        despesa: DespesaEntity
    )

    suspend fun delete(
        despesa: DespesaEntity
    )

    suspend fun findListByFechamento(
        fechamentoId: Long
    ): List<DespesaEntity>

    suspend fun existeDespesa(
        fechamentoId: Long,
        descricao: String
    ): Boolean
}