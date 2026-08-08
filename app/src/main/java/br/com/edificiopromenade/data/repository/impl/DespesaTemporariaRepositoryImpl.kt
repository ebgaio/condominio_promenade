package br.com.edificiopromenade.data.repository.impl

import br.com.edificiopromenade.data.local.dao.DespesaTemporariaDao
import br.com.edificiopromenade.data.local.entity.DespesaTemporariaEntity
import br.com.edificiopromenade.domain.repository.DespesaTemporariaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DespesaTemporariaRepositoryImpl @Inject constructor(

    private val dao: DespesaTemporariaDao

) : DespesaTemporariaRepository {

    override suspend fun insert(
        despesa: DespesaTemporariaEntity
    ): Long =
        dao.insert(despesa)

    override suspend fun update(
        despesa: DespesaTemporariaEntity
    ) =
        dao.update(despesa)

    override fun findAtivas():
            Flow<List<DespesaTemporariaEntity>> =
        dao.findAtivas()

    override suspend fun findById(
        id: Long
    ): DespesaTemporariaEntity? =
        dao.findById(id)

    override suspend fun encerrarDespesaTemporaria(
        id: Long
    ) =
        dao.encerrarDespesaTemporaria(id)
}