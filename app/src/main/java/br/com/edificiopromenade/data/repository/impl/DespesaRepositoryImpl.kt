package br.com.edificiopromenade.data.repository.impl

import br.com.edificiopromenade.data.local.dao.DespesaDao
import br.com.edificiopromenade.data.local.entity.DespesaEntity
import br.com.edificiopromenade.domain.repository.DespesaRepository
import jakarta.inject.Inject

class DespesaRepositoryImpl @Inject constructor(
    private val dao: DespesaDao
) : DespesaRepository {

    override fun findByFechamento(
        fechamentoId: Long
    ) =
        dao.findByFechamento(fechamentoId)

    override suspend fun insert(
        despesa: DespesaEntity
    ) =
        dao.insert(despesa)

    override suspend fun update(
        despesa: DespesaEntity
    ) =
        dao.update(despesa)

    override suspend fun delete(
        despesa: DespesaEntity
    ) =
        dao.delete(despesa)
}