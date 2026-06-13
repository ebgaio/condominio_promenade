package br.com.edificiopromenade.data.repository.impl

import br.com.edificiopromenade.data.local.dao.FechamentoMensalDao
import br.com.edificiopromenade.data.local.entity.FechamentoMensalEntity
import br.com.edificiopromenade.domain.repository.FechamentoRepository
import jakarta.inject.Inject

class FechamentoRepositoryImpl @Inject constructor(
    private val dao: FechamentoMensalDao
) : FechamentoRepository {

    override fun findAll() =
        dao.findAll()

    override suspend fun findByMesAno(
        mes: Int,
        ano: Int
    ) = dao.findByMesAno(mes, ano)

    override suspend fun findComDespesas(
        fechamentoId: Long
    ) = dao.findComDespesas(fechamentoId)

    override suspend fun findComDemonstrativos(
        fechamentoId: Long
    ) = dao.findComDemonstrativos(fechamentoId)

    override suspend fun insert(
        fechamento: FechamentoMensalEntity
    ) = dao.insert(fechamento)

    override suspend fun update(
        fechamento: FechamentoMensalEntity
    ) = dao.update(fechamento)
}