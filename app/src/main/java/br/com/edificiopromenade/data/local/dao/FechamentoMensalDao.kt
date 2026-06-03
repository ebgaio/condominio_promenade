package br.com.edificiopromenade.data.local.dao

import androidx.room.*
import br.com.edificiopromenade.data.local.entity.FechamentoMensalEntity
import br.com.edificiopromenade.data.local.relation.FechamentoComDemonstrativos
import br.com.edificiopromenade.data.local.relation.FechamentoComDespesas
import kotlinx.coroutines.flow.Flow

@Dao
interface FechamentoMensalDao {

    @Insert
    suspend fun insert(
        fechamento: FechamentoMensalEntity
    ): Long

    @Update
    suspend fun update(
        fechamento: FechamentoMensalEntity
    )

    @Query("""
        SELECT *
        FROM fechamentos_mensais
        ORDER BY ano DESC, mes DESC
    """)
    fun findAll(): Flow<List<FechamentoMensalEntity>>

    @Query("""
        SELECT *
        FROM fechamentos_mensais
        WHERE mes = :mes
        AND ano = :ano
        LIMIT 1
    """)
    suspend fun findByMesAno(
        mes: Int,
        ano: Int
    ): FechamentoMensalEntity?

    @Transaction
    @Query("""
    SELECT *
    FROM fechamentos_mensais
    WHERE id = :fechamentoId
""")
    suspend fun findComDespesas(
        fechamentoId: Long
    ): FechamentoComDespesas?

    @Transaction
    @Query("""
    SELECT *
    FROM fechamentos_mensais
    WHERE id = :fechamentoId
""")
    suspend fun findComDemonstrativos(
        fechamentoId: Long
    ): FechamentoComDemonstrativos?
}