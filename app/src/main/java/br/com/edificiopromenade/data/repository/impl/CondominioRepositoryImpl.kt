package br.com.edificiopromenade.data.repository.impl

import br.com.edificiopromenade.data.local.dao.CondominioDao
import br.com.edificiopromenade.data.local.entity.CondominioEntity
import br.com.edificiopromenade.domain.repository.CondominioRepository
import kotlinx.coroutines.flow.Flow
import jakarta.inject.Inject

class CondominioRepositoryImpl @Inject constructor(
    private val dao: CondominioDao
) : CondominioRepository {

    override fun findCondominioAtivo():
            Flow<CondominioEntity?> =
        dao.findCondominioAtivo()

    override suspend fun findAtivo() =
        dao.findAtivo()

    override fun findHistorico():
            Flow<List<CondominioEntity>> =
        dao.findHistorico()

    override suspend fun findById(
        id: Long
    ) = dao.findById(id)

    override suspend fun insert(
        condominio: CondominioEntity
    ) = dao.insert(condominio)

    override suspend fun update(
        condominio: CondominioEntity
    ) = dao.update(condominio)
}