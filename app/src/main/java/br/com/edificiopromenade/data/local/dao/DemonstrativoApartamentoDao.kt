package br.com.edificiopromenade.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.edificiopromenade.data.local.entity.DemonstrativoApartamentoEntity

@Dao
interface DemonstrativoApartamentoDao {

    @Insert
    suspend fun insert(
        demonstrativo: DemonstrativoApartamentoEntity
    ): Long

    @Insert
    suspend fun insertAll(
        demonstrativos: List<DemonstrativoApartamentoEntity>
    ): Long

    @Query("""
        DELETE
        FROM demonstrativos_apartamento
        WHERE fechamentoId = :fechamentoId
    """)
    suspend fun deletePorFechamento(
        fechamentoId: Long
    )

    @Query("""
        SELECT *
        FROM demonstrativos_apartamento
        WHERE fechamentoId = :fechamentoId
        ORDER BY numeroApartamentoHistorico
    """)
    suspend fun consultarPorFechamento(
        fechamentoId: Long
    ): List<DemonstrativoApartamentoEntity>
}