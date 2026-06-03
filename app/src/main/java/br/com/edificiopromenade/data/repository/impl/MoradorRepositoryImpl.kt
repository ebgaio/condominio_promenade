package br.com.edificiopromenade.data.repository.impl

import br.com.edificiopromenade.data.local.dao.MoradorDao
import br.com.edificiopromenade.data.local.entity.MoradorEntity
import br.com.edificiopromenade.domain.repository.MoradorRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MoradorRepositoryImpl @Inject constructor(
    private val dao: MoradorDao
) : MoradorRepository {

    override fun findAllAtivos() =
        dao.findAllAtivos()

    override suspend fun insert(
        morador: MoradorEntity
    ) = dao.insert(morador)

    override suspend fun update(
        morador: MoradorEntity
    ) = dao.update(morador)
}