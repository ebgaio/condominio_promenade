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
                val despesaParcelaId = parcela.despesaId

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

    override suspend fun excluirParcelaAvulsa(
        despesaId: Long
    ) {

        database.withTransaction {

            /*
             * =====================================================
             * 1. LOCALIZA A DESPESA MATERIALIZADA
             * =====================================================
             */
            val despesa =
                despesaDao.findById(
                    despesaId
                )
                    ?: return@withTransaction

            /*
             * =====================================================
             * 2. PROTEGE O FECHAMENTO ATUAL
             * =====================================================
             */
            val fechamentoAtual =
                fechamentoMensalDao.findById(
                    despesa.fechamentoId
                )
                    ?: throw IllegalStateException(
                        "Fechamento da despesa não encontrado."
                    )

            if (fechamentoAtual.finalizado) {

                throw IllegalStateException(
                    "Não é permitido excluir uma parcela " +
                            "de um fechamento finalizado."
                )
            }

            /*
             * =====================================================
             * 3. LOCALIZA A PARCELA DA DESPESA AVULSA
             * =====================================================
             */
            val parcelaSelecionada =
                despesaTemporariaParcelaDao
                    .findByDespesaId(
                        despesaId
                    )
                    ?: throw IllegalStateException(
                        "A despesa selecionada não pertence " +
                                "a uma despesa avulsa."
                    )

            val despesaTemporariaId = parcelaSelecionada.despesaTemporariaId

            /*
             * =====================================================
             * 4. LOCALIZA O PAI
             * =====================================================
             */
            val despesaTemporaria =
                despesaTemporariaDao.findById(
                    despesaTemporariaId
                )
                    ?: throw IllegalStateException(
                        "Despesa avulsa não encontrada."
                    )

            /*
             * =====================================================
             * 5. CARREGA TODAS AS PARCELAS
             * =====================================================
             */
            val parcelas =
                despesaTemporariaParcelaDao
                    .findByDespesaTemporariaId(
                        despesaTemporariaId
                    )

            /*
             * =====================================================
             * 6. PROTEÇÃO DO HISTÓRICO
             * =====================================================
             *
             * Se alguma parcela já pertence a um fechamento
             * finalizado, não podemos renumerar 1/3 para 1/2,
             * por exemplo.
             */
            for (parcela in parcelas) {

                val fechamentoId =
                    parcela.fechamentoId
                        ?: continue

                val fechamento =
                    fechamentoMensalDao.findById(
                        fechamentoId
                    )
                        ?: throw IllegalStateException(
                            "Fechamento da parcela " +
                                    "${parcela.numeroParcela} " +
                                    "não encontrado."
                        )

                if (fechamento.finalizado) {

                    throw IllegalStateException(
                        "Não é possível excluir somente uma parcela " +
                                "porque esta despesa já possui parcela " +
                                "em fechamento finalizado."
                    )
                }
            }

            /*
             * =====================================================
             * 7. EXCLUI A PARCELA SELECIONADA
             * =====================================================
             *
             * Primeiro a parcela temporária.
             *
             * Assim não deixamos o SET_NULL da FK ser
             * responsável pela regra de negócio.
             */
            despesaTemporariaParcelaDao.deleteById(
                parcelaSelecionada.id
            )

            /*
             * Remove a DespesaEntity materializada.
             *
             * Os DespesaItemEntity relacionados serão
             * eliminados automaticamente por CASCADE.
             */
            despesaDao.deleteById(
                despesaId
            )

            /*
             * =====================================================
             * 8. OBTÉM AS PARCELAS QUE SOBRARAM
             * =====================================================
             */
            val parcelasRestantes =
                parcelas
                    .filter {
                        it.id != parcelaSelecionada.id
                    }
                    .sortedBy {
                        it.numeroParcela
                    }

            /*
             * =====================================================
             * 9. ERA A ÚLTIMA PARCELA?
             * =====================================================
             */
            if (parcelasRestantes.isEmpty()) {

                /*
                 * Não existe mais qualquer parcela.
                 *
                 * Portanto o pai também não possui mais
                 * finalidade.
                 */
                despesaTemporariaDao.deleteById(
                    despesaTemporariaId
                )

                /*
                 * Remove eventual demonstrativo residual
                 * de uma tentativa incompleta de finalização.
                 */
                demonstrativoApartamentoDao
                    .deletePorFechamento(
                        fechamentoAtual.id
                    )

                return@withTransaction
            }

            /*
             * =====================================================
             * 10. NOVA QUANTIDADE
             * =====================================================
             */
            val novaQuantidade = parcelasRestantes.size

            /*
             * =====================================================
             * 11. RENUMERA AS PARCELAS
             * =====================================================
             *
             * Competência NÃO muda.
             *
             * Exemplo:
             *
             * antes:
             * 09 = 1/3
             * 10 = 2/3  <- excluída
             * 11 = 3/3
             *
             * depois:
             * 09 = 1/2
             * 11 = 2/2
             */
            parcelasRestantes
                .forEachIndexed {
                        indice,
                        parcela ->

                    val novoNumero = indice + 1

                    val parcelaAtualizada =
                        parcela.copy(
                            numeroParcela = novoNumero
                        )

                    despesaTemporariaParcelaDao
                        .update(
                            parcelaAtualizada
                        )

                    /*
                     * Se essa parcela já estiver materializada
                     * em algum fechamento aberto, também temos
                     * de corrigir descricaoLivre.
                     */
                    val despesaParcelaId = parcela.despesaId

                    if (despesaParcelaId != null) {

                        val despesaParcela =
                            despesaDao.findById(
                                despesaParcelaId
                            )
                                ?: throw IllegalStateException(
                                    "Despesa da parcela " +
                                            "${parcela.numeroParcela} " +
                                            "não encontrada."
                                )

                        val novaDescricao =
                            if (novaQuantidade == 1) {

                                despesaTemporaria.descricao

                            } else {

                                "${despesaTemporaria.descricao} - " +
                                        "Parcela $novoNumero/$novaQuantidade"
                            }

                        despesaDao.update(
                            despesaParcela.copy(
                                descricaoLivre = novaDescricao
                            )
                        )
                    }
                }

            /*
             * =====================================================
             * 12. ATUALIZA O PAI
             * =====================================================
             */

            /*
             * Uma despesa permanece ativa enquanto ainda
             * existir alguma parcela futura não associada.
             */
            val possuiParcelaPendente =
                parcelasRestantes.any { parcela ->

                    parcela.fechamentoId == null && !parcela.paga
                }

            despesaTemporariaDao.update(
                despesaTemporaria.copy(
                    quantidadeParcelas = novaQuantidade,
                    ativa = possuiParcelaPendente
                )
            )

            /*
             * =====================================================
             * 13. LIMPA RESULTADOS DERIVADOS DOS FECHAMENTOS
             *     ABERTOS AFETADOS
             * =====================================================
             */
            val fechamentosAfetados =
                buildSet {

                    add(
                        fechamentoAtual.id
                    )

                    parcelasRestantes
                        .mapNotNull {
                            it.fechamentoId
                        }
                        .forEach {
                            add(it)
                        }
                }

            fechamentosAfetados
                .forEach { fechamentoId ->

                    demonstrativoApartamentoDao
                        .deletePorFechamento(
                            fechamentoId
                        )
                }
        }
    }
}