package br.com.edificiopromenade.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.edificiopromenade.data.local.entity.CondominioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CondominioDao {

    @Insert
    suspend fun insert(
        condominio: CondominioEntity
    ): Long

    @Update
    suspend fun update(
        condominio: CondominioEntity
    )

    @Query("""
        SELECT *
        FROM condominios
        WHERE ativo = 1
        LIMIT 1
    """)
    fun findCondominioAtivo():
            Flow<CondominioEntity?>

    @Query("""
        SELECT *
        FROM condominios
        WHERE ativo = 1
        LIMIT 1
    """)
    suspend fun findAtivo():
            CondominioEntity?

    @Query("""
        SELECT *
        FROM condominios
        WHERE id = :id
    """)
    suspend fun findById(
        id: Long
    ): CondominioEntity?

    @Query("""
        SELECT *
        FROM condominios
        ORDER BY dataCriacao DESC
    """)
    fun findHistorico():
            Flow<List<CondominioEntity>>
}