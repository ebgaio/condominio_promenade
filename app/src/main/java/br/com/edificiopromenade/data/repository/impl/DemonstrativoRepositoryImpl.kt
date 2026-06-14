package br.com.edificiopromenade.data.repository.impl

import br.com.edificiopromenade.data.local.dao.DemonstrativoApartamentoDao
import br.com.edificiopromenade.data.local.entity.DemonstrativoApartamentoEntity
import br.com.edificiopromenade.domain.repository.DemonstrativoRepository
import jakarta.inject.Inject

class DemonstrativoRepositoryImpl @Inject constructor(
    private val dao: DemonstrativoApartamentoDao
) : DemonstrativoRepository {

    override suspend fun insert(
        demonstrativo: DemonstrativoApartamentoEntity
    ) = dao.insert(demonstrativo)

    override suspend fun deletePorFechamento(
        fechamentoId: Long
    ) = dao.deletePorFechamento(
            fechamentoId
        )
}