package br.com.edificiopromenade.domain.repository

import br.com.edificiopromenade.data.local.entity.DespesaTemporariaParcelaEntity
import kotlinx.coroutines.flow.Flow

interface DespesaTemporariaParcelaRepository {

    suspend fun insert(
        parcela: DespesaTemporariaParcelaEntity
    ): Long

    suspend fun insertAll(
        parcelas: List<DespesaTemporariaParcelaEntity>
    )

    suspend fun update(
        parcela: DespesaTemporariaParcelaEntity
    )

    suspend fun buscarParcelasDaCompetencia(
        competencia: Int
    ): List<DespesaTemporariaParcelaEntity>

    fun buscarPorFechamento(
        fechamentoId: Long
    ): Flow<List<DespesaTemporariaParcelaEntity>>

    suspend fun marcarComoPagas(
        fechamentoId: Long
    )

    suspend fun existeParcelaPendente(
        despesaTemporariaId: Long
    ): Boolean

    suspend fun associarAoFechamentoEDespesa(
        parcelaId: Long,
        fechamentoId: Long,
        despesaId: Long
    ): Boolean
}