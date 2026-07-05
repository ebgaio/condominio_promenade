package br.com.edificiopromenade.data.repository.impl

import br.com.edificiopromenade.data.local.dao.DespesaDao
import br.com.edificiopromenade.data.local.entity.DespesaComItensEntity
import br.com.edificiopromenade.data.local.entity.DespesaComTipoEntity
import br.com.edificiopromenade.data.local.entity.DespesaEntity
import br.com.edificiopromenade.domain.repository.DespesaRepository
import jakarta.inject.Inject

class DespesaRepositoryImpl @Inject constructor(
    private val dao: DespesaDao
) : DespesaRepository {

    override fun findByFechamento(
        fechamentoId: Long
    ) = dao.findByFechamento(fechamentoId)

    override suspend fun insert(
        despesa: DespesaEntity
    ) = dao.insert(despesa)

    override suspend fun update(
        despesa: DespesaEntity
    ) = dao.update(despesa)

    override suspend fun deleteById(
        id: Long
    ) = dao.deleteById(id)

    override suspend fun findListByFechamento(
        fechamentoId: Long
    ) = dao.findListByFechamento(fechamentoId)

    override suspend fun existeDespesa(
        fechamentoId: Long,
        tipoDespesaId: Long
    ): Boolean {
        return dao.countByDescricao(
            fechamentoId,
            tipoDespesaId
        ) > 0
    }

    override suspend fun findComTipoByFechamento(
        fechamentoId: Long
    ): List<DespesaComTipoEntity> {
        return dao.findComTipoByFechamento(
            fechamentoId
        )
    }

    override fun findComTipoByFechamentoFlow(
        fechamentoId: Long
    ) = dao.findComTipoByFechamentoFlow(
            fechamentoId
        )

    override suspend fun findById(
        id: Long
    ) = dao.findById(id)

    override suspend fun findComItens(
        despesaId: Long
    ): DespesaComItensEntity? {

        return dao.findComItens(
            despesaId
        )
    }

    override fun findComItensByFechamento(
        fechamentoId: Long
    ) = dao.findComItensByFechamento(
            fechamentoId
        )

    override suspend fun findComItensListByFechamento(
        fechamentoId: Long
    ): List<DespesaComItensEntity> {
        return dao.findComItensListByFechamento(
            fechamentoId
        )
    }

    override suspend fun atualizarValor(
        despesaId: Long,
        novoValor: Double
    ) {
        dao.atualizarValor(
            despesaId,
            novoValor
        )
    }
}