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

    override suspend fun salvarTodos(
        demonstrativos: List<DemonstrativoApartamentoEntity>
    ) {
        dao.insertAll(demonstrativos)
    }

    override suspend fun excluirPorFechamento(
        fechamentoId: Long
    ) = dao.deletePorFechamento(
        fechamentoId
    )

    override suspend fun consultarPorFechamento(
        fechamentoId: Long
    ) = dao.consultarPorFechamento(fechamentoId)
}