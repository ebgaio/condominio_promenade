package br.com.edificiopromenade.domain.repository

import br.com.edificiopromenade.data.local.entity.DespesaTemporariaEntity
import kotlinx.coroutines.flow.Flow

interface DespesaTemporariaRepository {

    suspend fun insert(
        despesa: DespesaTemporariaEntity
    ): Long

    suspend fun update(
        despesa: DespesaTemporariaEntity
    )

    fun findAtivas():
            Flow<List<DespesaTemporariaEntity>>

    suspend fun findById(
        id: Long
    ): DespesaTemporariaEntity?

    suspend fun encerrarDespesaTemporaria(
        id: Long
    )
}