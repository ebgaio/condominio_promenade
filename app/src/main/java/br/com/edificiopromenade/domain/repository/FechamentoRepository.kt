package br.com.edificiopromenade.domain.repository

import br.com.edificiopromenade.data.local.entity.FechamentoMensalEntity
import br.com.edificiopromenade.data.local.relation.FechamentoComDemonstrativos
import br.com.edificiopromenade.data.local.relation.FechamentoComDespesas
import kotlinx.coroutines.flow.Flow

interface FechamentoRepository {

    fun findAll():
            Flow<List<FechamentoMensalEntity>>

    suspend fun findByMesAno(
        mes: Int,
        ano: Int
    ): FechamentoMensalEntity?

    suspend fun findComDespesas(
        fechamentoId: Long
    ): FechamentoComDespesas?

    suspend fun findComDemonstrativos(
        fechamentoId: Long
    ): FechamentoComDemonstrativos?

    suspend fun insert(
        fechamento: FechamentoMensalEntity
    ): Long

    suspend fun update(
        fechamento: FechamentoMensalEntity
    )
}