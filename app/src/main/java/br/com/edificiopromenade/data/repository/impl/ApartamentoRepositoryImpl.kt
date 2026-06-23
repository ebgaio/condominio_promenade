package br.com.edificiopromenade.data.repository.impl

import br.com.edificiopromenade.data.local.dao.ApartamentoDao
import br.com.edificiopromenade.data.local.entity.ApartamentoEntity
import br.com.edificiopromenade.domain.repository.ApartamentoRepository
import jakarta.inject.Inject

class ApartamentoRepositoryImpl @Inject constructor(
    private val dao: ApartamentoDao
) : ApartamentoRepository {

    override fun findAll() =
        dao.findAll()

    override fun findAllComMoradores() =
        dao.findAllComMoradores()

    override suspend fun findDetalhado(id: Long) =
        dao.findDetalhado(id)

    override suspend fun findById(id: Long) =
        dao.findById(id)

    override suspend fun insert(
        apartamento: ApartamentoEntity
    ) = dao.insert(apartamento)

    override suspend fun update(
        apartamento: ApartamentoEntity
    ) = dao.update(apartamento)

    override suspend fun inativar(
        id: Long
    ) = dao.inativar(id)

    override suspend fun findAllAtivosList() =
        dao.findAllAtivosList()

    override suspend fun existeApartamento(
        numero: String
    ): Boolean {
        return dao.countByDescricao(
            numero
        ) > 0
    }
}