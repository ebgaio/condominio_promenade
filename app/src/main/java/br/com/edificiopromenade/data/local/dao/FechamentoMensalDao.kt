package br.com.edificiopromenade.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
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

    @Query("""
        SELECT *
        FROM fechamentos_mensais
        WHERE id = :id
        LIMIT 1
    """)
    suspend fun findById(
        id: Long
    ): FechamentoMensalEntity?

    @Query("""
        SELECT COUNT(*)
        FROM fechamentos_mensais
        WHERE finalizado = :finalizado
    """)
    suspend fun countByDescricao(
        finalizado: Boolean
    ): Int

    @Query("""
        SELECT *
        FROM fechamentos_mensais
        WHERE ano < :ano
           OR (ano = :ano AND mes < :mes)
        ORDER BY ano DESC, mes DESC
        LIMIT 1
    """)
    suspend fun findAnterior(
        mes: Int,
        ano: Int
    ): FechamentoMensalEntity?

    @Query("""
        SELECT *
        FROM fechamentos_mensais
        WHERE ano > :ano
           OR (ano = :ano AND mes > :mes)
        ORDER BY ano ASC, mes ASC
        LIMIT 1
    """)
    suspend fun findProximo(
        mes: Int,
        ano: Int
    ): FechamentoMensalEntity?
}