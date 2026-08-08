package br.com.edificiopromenade.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.edificiopromenade.data.local.entity.DespesaTemporariaParcelaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DespesaTemporariaParcelaDao {

    @Insert
    suspend fun insert(
        parcela: DespesaTemporariaParcelaEntity
    ): Long

    @Insert
    suspend fun insertAll(
        parcelas: List<DespesaTemporariaParcelaEntity>
    )

    @Update
    suspend fun update(
        parcela: DespesaTemporariaParcelaEntity
    )


    @Query("""
        SELECT *
        FROM despesas_temporarias_parcelas
        WHERE competencia = :competencia
          AND fechamentoId IS NULL
          AND despesaId IS NULL
          AND paga = 0
        ORDER BY numeroParcela
    """)
    suspend fun findParcelasDaCompetencia(
        competencia: Int
    ): List<DespesaTemporariaParcelaEntity>

    @Query("""
        SELECT *
        FROM despesas_temporarias_parcelas
        WHERE fechamentoId = :fechamentoId
        ORDER BY numeroParcela
    """)
    fun findByFechamento(
        fechamentoId: Long
    ): Flow<List<DespesaTemporariaParcelaEntity>>

    @Query("""
        UPDATE despesas_temporarias_parcelas
        SET paga = 1
        WHERE fechamentoId = :fechamentoId
    """)
    suspend fun marcarParcelasComoPagas(
        fechamentoId: Long
    )

    @Query("""
        SELECT *
        FROM despesas_temporarias_parcelas
        WHERE despesaTemporariaId = :despesaId
        ORDER BY numeroParcela
    """)
    suspend fun findByDespesa(
        despesaId: Long
    ): List<DespesaTemporariaParcelaEntity>

    @Query("""
        SELECT EXISTS(
            SELECT 1
            FROM despesas_temporarias_parcelas
            WHERE despesaTemporariaId = :despesaTemporariaId
              AND fechamentoId IS NULL
              AND paga = 0
        )
    """)
    suspend fun existeParcelaPendente(
        despesaTemporariaId: Long
    ): Boolean

    @Query("""
        UPDATE despesas_temporarias_parcelas
           SET fechamentoId = :fechamentoId,
               despesaId = :despesaId
         WHERE id = :parcelaId
           AND fechamentoId IS NULL
           AND despesaId IS NULL
    """)
    suspend fun associarAoFechamentoEDespesa(
        parcelaId: Long,
        fechamentoId: Long,
        despesaId: Long
    ): Int
}