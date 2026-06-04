package br.com.edificiopromenade.domain.repository

import br.com.edificiopromenade.data.local.entity.CondominioEntity
import kotlinx.coroutines.flow.Flow

interface CondominioRepository {

    fun findCondominioAtivo():
            Flow<CondominioEntity?>

    fun findHistorico():
            Flow<List<CondominioEntity>>

    suspend fun findById(
        id: Long
    ): CondominioEntity?

    suspend fun insert(
        condominio: CondominioEntity
    ): Long

    suspend fun update(
        condominio: CondominioEntity
    )
}