package br.com.edificiopromenade.data.repository.impl

import br.com.edificiopromenade.data.local.dao.TipoDespesaDao
import br.com.edificiopromenade.data.local.entity.TipoDespesaEntity
import br.com.edificiopromenade.domain.repository.TipoDespesaRepository
import jakarta.inject.Inject

class TipoDespesaRepositoryImpl @Inject constructor(
    private val dao: TipoDespesaDao
) : TipoDespesaRepository {

    override fun findAllAtivos() =
        dao.findAllAtivos()

    override suspend fun findById(
        id: Long
    ) = dao.findById(id)

    override suspend fun insert(
        tipo: TipoDespesaEntity
    ) = dao.insert(tipo)

    override suspend fun update(
        tipo: TipoDespesaEntity
    ) = dao.update(tipo)
}