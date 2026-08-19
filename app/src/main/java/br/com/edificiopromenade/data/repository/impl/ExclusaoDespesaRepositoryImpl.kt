package br.com.edificiopromenade.data.repository.impl

import androidx.room.withTransaction
import br.com.edificiopromenade.data.local.dao.DemonstrativoApartamentoDao
import br.com.edificiopromenade.data.local.dao.DespesaDao
import br.com.edificiopromenade.data.local.dao.DespesaTemporariaDao
import br.com.edificiopromenade.data.local.dao.DespesaTemporariaParcelaDao
import br.com.edificiopromenade.data.local.dao.FechamentoMensalDao
import br.com.edificiopromenade.data.local.database.AppDatabase
import br.com.edificiopromenade.domain.repository.ExclusaoDespesaRepository
import jakarta.inject.Inject

class ExclusaoDespesaRepositoryImpl @Inject constructor(
    private val database: AppDatabase,
    private val despesaDao: DespesaDao,
    private val despesaTemporariaDao: DespesaTemporariaDao,
    private val despesaTemporariaParcelaDao: DespesaTemporariaParcelaDao,
    private val fechamentoMensalDao: FechamentoMensalDao,
    private val demonstrativoApartamentoDao: DemonstrativoApartamentoDao
) : ExclusaoDespesaRepository {

    override suspend fun excluirRascunho(
        despesaId: Long
    ) {
        database.withTransaction {

            /*
             * =================================================
             * 1. LOCALIZA A DESPESA
             * =================================================
             */
            val despesa = despesaDao.findById(
                    despesaId
                )
                    ?: return@withTransaction

            /*
             * =================================================
             * 2. PROTEGE FECHAMENTO FINALIZADO
             * =================================================
             */
            val fechamentoAtual = fechamentoMensalDao.findById(
                    despesa.fechamentoId
                )
                    ?: throw IllegalStateException(
                        "Fechamento da despesa não encontrado."
                    )

            if (fechamentoAtual.finalizado) {

                throw IllegalStateException(
                    "Não é permitido excluir uma despesa " +
                        "de um fechamento finalizado."
                )
            }

            /*
             * =================================================
             * 3. VERIFICA SE É UMA DESPESA AVULSA
             * =================================================
             *
             * Uma despesa fixa não possui registro em:
             *
             * despesas_temporarias_parcelas
             */
            val parcelaAtual = despesaTemporariaParcelaDao
                    .findByDespesaId(
                        despesaId
                    )

            /*
             * =================================================
             * DESPESA FIXA
             * =================================================
             */
            if (parcelaAtual == null) {

                /*
                 * Demonstrativos não deveriam existir
                 * em um fechamento aberto.
                 *
                 * Mas, caso uma tentativa anterior de
                 * finalização tenha ficado incompleta,
                 * removemos qualquer resultado derivado.
                 */
                demonstrativoApartamentoDao
                    .deletePorFechamento(
                        fechamentoAtual.id
                    )

                /*
                 * DespesaItemEntity será removido
                 * automaticamente por CASCADE.
                 */
                despesaDao.deleteById(
                    despesaId
                )

                return@withTransaction
            }

            /*
             * =================================================
             * DESPESA AVULSA
             * =================================================
             */

            val despesaTemporariaId = parcelaAtual.despesaTemporariaId

            val parcelas = despesaTemporariaParcelaDao
                    .findByDespesaTemporariaId(
                        despesaTemporariaId
                    )

            var possuiHistorico = false

            val fechamentosAbertosAfetados = mutableSetOf<Long>()

            /*
             * =================================================
             * 4. ANALISA TODAS AS PARCELAS
             * =================================================
             */
            for (parcela in parcelas) {

                val fechamentoId = parcela.fechamentoId

                /*
                 * ---------------------------------------------
                 * PARCELA MATERIALIZADA
                 * ---------------------------------------------
                 */
                if (fechamentoId != null) {

                    val fechamento = fechamentoMensalDao.findById(
                            fechamentoId
                        )
                            ?: throw IllegalStateException(
                                "Fechamento da parcela " +
                                    "${parcela.numeroParcela} " +
                                    "não encontrado."
                            )

                    /*
                     * Histórico é intocável.
                     */
                    if (fechamento.finalizado) {
                        possuiHistorico = true
                        continue
                    }

                    /*
                     * Fechamento aberto será afetado
                     * pela exclusão.
                     */
                    fechamentosAbertosAfetados.add(
                        fechamentoId
                    )
                }

                /*
                 * ---------------------------------------------
                 * ABERTA OU FUTURA
                 * ---------------------------------------------
                 *
                 * Primeiro eliminamos a parcela.
                 *
                 * Assim não dependemos do SET_NULL
                 * para executar a nossa regra de negócio.
                 */
                val despesaParcelaId =
                    parcela.despesaId

                despesaTemporariaParcelaDao
                    .deleteById(
                        parcela.id
                    )

                /*
                 * Se já havia sido materializada,
                 * elimina sua DespesaEntity.
                 *
                 * Seus itens desaparecerão por CASCADE.
                 */
                if (despesaParcelaId != null) {

                    despesaDao.deleteById(
                        despesaParcelaId
                    )
                }
            }

            /*
             * =================================================
             * 5. REMOVE DADOS DERIVADOS DE FECHAMENTOS ABERTOS
             * =================================================
             */
            fechamentosAbertosAfetados
                .forEach { fechamentoId ->
                    demonstrativoApartamentoDao
                        .deletePorFechamento(
                            fechamentoId
                        )
                }

            /*
             * =================================================
             * 6. TRATA O PAI TEMPORÁRIO
             * =================================================
             */
            if (possuiHistorico) {

                /*
                 * Há pelo menos uma parcela pertencente
                 * a fechamento finalizado.
                 *
                 * Portanto o pai continua existindo
                 * para preservar a cadeia histórica,
                 * mas deixa de gerar parcelas futuras.
                 */
                despesaTemporariaDao
                    .encerrarDespesaTemporaria(
                        despesaTemporariaId
                    )

            } else {

                /*
                 * Nenhuma parcela virou histórico.
                 *
                 * O registro pai não tem mais qualquer
                 * utilidade e pode ser eliminado.
                 */
                despesaTemporariaDao
                    .deleteById(
                        despesaTemporariaId
                    )
            }
        }
    }
}