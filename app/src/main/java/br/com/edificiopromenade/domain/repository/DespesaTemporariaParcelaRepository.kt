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

    suspend fun buscarListaPorFechamento(
        fechamentoId: Long
    ): List<DespesaTemporariaParcelaEntity>

    /*
     * NOVO:
     * considera qualquer parcela paga = false,
     * esteja ela associada ou não a um fechamento.
     */
    suspend fun existeParcelaNaoPaga(
        despesaTemporariaId: Long
    ): Boolean

    suspend fun associarAoFechamentoEDespesa(
        parcelaId: Long,
        fechamentoId: Long,
        despesaId: Long
    ): Boolean

    suspend fun atualizarValorPorDespesaId(
        despesaId: Long,
        valor: Double
    )

    suspend fun findByDespesaId(
        despesaId: Long
    ): DespesaTemporariaParcelaEntity?
}