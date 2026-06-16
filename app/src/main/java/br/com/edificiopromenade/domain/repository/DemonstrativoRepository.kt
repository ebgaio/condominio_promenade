package br.com.edificiopromenade.domain.repository

import br.com.edificiopromenade.data.local.entity.DemonstrativoApartamentoEntity

interface DemonstrativoRepository {

    suspend fun insert(
        demonstrativo: DemonstrativoApartamentoEntity
    ): Long

    suspend fun salvarTodos(
        demonstrativos: List<DemonstrativoApartamentoEntity>
    )

    suspend fun excluirPorFechamento(
        fechamentoId: Long
    )

    suspend fun consultarPorFechamento(
        fechamentoId: Long
    ): List<DemonstrativoApartamentoEntity>
}