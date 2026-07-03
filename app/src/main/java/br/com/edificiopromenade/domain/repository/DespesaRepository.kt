package br.com.edificiopromenade.domain.repository

import br.com.edificiopromenade.data.local.entity.DespesaComItensEntity
import br.com.edificiopromenade.data.local.entity.DespesaComTipoEntity
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

    suspend fun deleteById(
        id: Long
    )

    suspend fun findListByFechamento(
        fechamentoId: Long
    ): List<DespesaEntity>

    suspend fun existeDespesa(
        fechamentoId: Long,
        tipoDespesaId: Long
    ): Boolean

    suspend fun findComTipoByFechamento(
        fechamentoId: Long
    ): List<DespesaComTipoEntity>

    fun findComTipoByFechamentoFlow(
        fechamentoId: Long
    ): Flow<List<DespesaComTipoEntity>>

    suspend fun findById(
        id: Long
    ): DespesaEntity?

    suspend fun findComItens(
        despesaId: Long
    ): DespesaComItensEntity?

    fun findComItensByFechamento(
        fechamentoId: Long
    ): Flow<List<DespesaComItensEntity>>

    suspend fun findComItensListByFechamento(
        fechamentoId: Long
    ): List<DespesaComItensEntity>
}