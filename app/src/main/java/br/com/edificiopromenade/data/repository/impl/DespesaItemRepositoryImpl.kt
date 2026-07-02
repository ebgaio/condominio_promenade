package br.com.edificiopromenade.data.repository.impl

import br.com.edificiopromenade.data.local.dao.DespesaItemDao
import br.com.edificiopromenade.data.local.entity.DespesaItemEntity
import br.com.edificiopromenade.domain.repository.DespesaItemRepository
import jakarta.inject.Inject

class DespesaItemRepositoryImpl @Inject constructor(

    private val dao: DespesaItemDao

) : DespesaItemRepository {

    override suspend fun insert(
        item: DespesaItemEntity
    ) = dao.insert(item)

    override suspend fun update(
        item: DespesaItemEntity
    ) = dao.update(item)

    override suspend fun delete(
        item: DespesaItemEntity
    ) = dao.delete(item)

    override fun findByDespesa(
        despesaId: Long
    ) = dao.findByDespesa(despesaId)

    override suspend fun findListByDespesa(
        despesaId: Long
    ) = dao.findListByDespesa(despesaId)

    override suspend fun calcularTotalItens(
        despesaId: Long
    ) = dao.calcularTotalItens(despesaId) ?: 0.0
}