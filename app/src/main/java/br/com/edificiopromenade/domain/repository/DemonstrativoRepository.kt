package br.com.edificiopromenade.domain.repository

import br.com.edificiopromenade.data.local.entity.DemonstrativoApartamentoEntity

interface DemonstrativoRepository {

    suspend fun insert(
        demonstrativo: DemonstrativoApartamentoEntity
    ): Long

    suspend fun deletePorFechamento(
        fechamentoId: Long
    )
}