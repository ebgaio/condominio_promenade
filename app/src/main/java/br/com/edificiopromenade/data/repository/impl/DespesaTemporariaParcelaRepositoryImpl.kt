package br.com.edificiopromenade.data.repository.impl

import br.com.edificiopromenade.data.local.dao.DespesaTemporariaParcelaDao
import br.com.edificiopromenade.data.local.entity.DespesaTemporariaParcelaEntity
import br.com.edificiopromenade.domain.repository.DespesaTemporariaParcelaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DespesaTemporariaParcelaRepositoryImpl @Inject constructor(

    private val dao: DespesaTemporariaParcelaDao

) : DespesaTemporariaParcelaRepository {

    override suspend fun insert(
        parcela: DespesaTemporariaParcelaEntity
    ): Long =
        dao.insert(parcela)

    override suspend fun insertAll(
        parcelas: List<DespesaTemporariaParcelaEntity>
    ) =
        dao.insertAll(parcelas)

    override suspend fun update(
        parcela: DespesaTemporariaParcelaEntity
    ) =
        dao.update(parcela)

    override suspend fun buscarParcelasDaCompetencia(
        competencia: Int
    ): List<DespesaTemporariaParcelaEntity> =
        dao.findParcelasDaCompetencia(competencia)

    override fun buscarPorFechamento(
        fechamentoId: Long
    ): Flow<List<DespesaTemporariaParcelaEntity>> =
        dao.findByFechamento(fechamentoId)

    override suspend fun marcarComoPagas(
        fechamentoId: Long
    ) =
        dao.marcarParcelasComoPagas(fechamentoId)

    override suspend fun existeParcelaPendente(
        despesaTemporariaId: Long
    ): Boolean {
        return dao.existeParcelaPendente(
            despesaTemporariaId
        )
    }

    override suspend fun associarAoFechamentoEDespesa(
        parcelaId: Long,
        fechamentoId: Long,
        despesaId: Long
    ): Boolean {
        return dao.associarAoFechamentoEDespesa(
            parcelaId = parcelaId,
            fechamentoId = fechamentoId,
            despesaId = despesaId
        ) > 0
    }
}