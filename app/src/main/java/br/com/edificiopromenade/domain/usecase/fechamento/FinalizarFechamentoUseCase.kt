package br.com.edificiopromenade.domain.usecase.fechamento

import br.com.edificiopromenade.domain.repository.DespesaRepository
import br.com.edificiopromenade.domain.repository.DespesaTemporariaParcelaRepository
import br.com.edificiopromenade.domain.repository.DespesaTemporariaRepository
import br.com.edificiopromenade.domain.repository.FechamentoRepository
import br.com.edificiopromenade.domain.repository.TransacaoRepository
import br.com.edificiopromenade.domain.usecase.demonstrativo.GerarDemonstrativosUseCase
import jakarta.inject.Inject
import java.time.YearMonth

class FinalizarFechamentoUseCase @Inject constructor(
    private val fechamentoRepository: FechamentoRepository,
    private val gerarDemonstrativosUseCase: GerarDemonstrativosUseCase,
    private val despesaRepository: DespesaRepository,
    private val despesaTemporariaParcelaRepository: DespesaTemporariaParcelaRepository,
    private val despesaTemporariaRepository: DespesaTemporariaRepository,
    private val transacaoRepository: TransacaoRepository
) {
    suspend operator fun invoke(
        fechamentoId: Long
    ) {

        transacaoRepository.executar {

            /*
             * =====================================================
             * 1. LOCALIZA O FECHAMENTO DENTRO DA TRANSAÇÃO
             * =====================================================
             *
             * É importante fazer a leitura aqui dentro.
             */
            val fechamento = fechamentoRepository
                .findById(
                    fechamentoId
                )
                    ?: throw IllegalStateException(
                        "Fechamento não encontrado."
                    )

            /*
             * =====================================================
             * 2. FECHAMENTO JÁ FINALIZADO
             * =====================================================
             *
             * Um fechamento finalizado é imutável.
             *
             * Portanto não refazemos snapshot,
             * demonstrativos nem qualquer outra consolidação.
             */
            if (fechamento.finalizado) {
                return@executar
            }

            /*
             * =====================================================
             * 3. PROTEGE COMPETÊNCIA FUTURA
             * =====================================================
             *
             * A data utilizada é a data atual do aparelho.
             *
             * Fechamentos futuros podem existir para receber
             * parcelas de despesas avulsas, mas não podem ser
             * finalizados antes da respectiva competência.
             */
            val competenciaFechamento = YearMonth.of(
                    fechamento.ano,
                    fechamento.mes
                )

            val competenciaAtual = YearMonth.now()

            if (
                competenciaFechamento.isAfter(
                        competenciaAtual
                    )
            ) {
                throw IllegalStateException(
                    "Não é permitido finalizar um fechamento de competência futura."
                )
            }

            /*
            * =====================================================
            * 4. CARREGA AS DESPESAS COM SEUS TIPOS
            * =====================================================
            *
            * Não usamos mais apenas findListByFechamento()
            * neste ponto.
            *
            * Precisamos também do TipoDespesaEntity atual
            * para congelar:
            *
            * - descrição utilizada;
            * - regra usaFracaoIdeal utilizada.
            */
            val despesasComTipo =
                despesaRepository
                    .findComTipoByFechamento(
                        fechamentoId
                    )

            if (despesasComTipo.isEmpty()) {

                throw IllegalStateException(
                    "Não existem despesas cadastradas."
                )
            }

            /*
             * =====================================================
             * 5. GRAVA O SNAPSHOT HISTÓRICO DAS DESPESAS
             * =====================================================
             *
             * A partir deste instante, os dados relevantes
             * vindos de TipoDespesa deixam de depender do
             * catálogo atual.
             *
             * O snapshot ainda está dentro da transação.
             *
             * Se qualquer etapa posterior falhar,
             * estes UPDATEs também serão revertidos.
             */
            despesasComTipo.forEach {
                    despesaComTipo ->

                val despesa = despesaComTipo.despesa

                val tipo = despesaComTipo.tipo

                /*
                 * A Despesa Avulsa utiliza sua própria
                 * descricaoLivre.
                 *
                 * Exemplo:
                 *
                 * Pintura - Parcela 2/3
                 *
                 * As despesas fixas utilizam o nome
                 * existente no TipoDespesa no momento
                 * da finalização.
                 */
                val avulsa =
                    tipo.descricao.equals(
                        "Despesa Avulsa",
                        ignoreCase = true
                    )

                val descricaoHistorica =
                    if (avulsa) {

                        despesa.descricaoLivre
                            .ifBlank {
                                tipo.descricao
                            }

                    } else {
                        tipo.descricao
                    }

                despesaRepository.update(
                    despesa.copy(

                        descricaoHistorica = descricaoHistorica,
                        usaFracaoIdealHistorica = tipo.usaFracaoIdeal
                    )
                )
            }

            /*
             * =====================================================
             * 6. GERA OS DEMONSTRATIVOS
             * =====================================================
             *
             * Neste momento os snapshots já estão gravados
             * dentro da transação.
             *
             * Como o mapper de domínio agora prioriza:
             *
             * descricaoHistorica
             * usaFracaoIdealHistorica
             *
             * o cálculo definitivo usa exatamente os dados
             * que ficarão congelados no fechamento.
             */
            val demonstrativos = gerarDemonstrativosUseCase(
                    fechamentoId = fechamentoId
                )

            /*
             * Uma lista vazia significaria que o fechamento
             * seria marcado como finalizado sem resultado
             * definitivo para os apartamentos.
             */
            if (demonstrativos.isEmpty()) {

                throw IllegalStateException(
                    "Não foi possível gerar os demonstrativos do fechamento."
                )
            }

            /*
             * =====================================================
             * 7. LOCALIZA AS PARCELAS AVULSAS DESTE FECHAMENTO
             * =====================================================
             *
             * Fazemos isso ANTES de marcá-las como pagas,
             * pois precisamos descobrir quais
             * DespesaTemporariaEntity devem ser verificadas.
             */
            val parcelasAvulsas = despesaTemporariaParcelaRepository
                    .buscarListaPorFechamento(
                        fechamentoId
                    )

            /*
             * Uma mesma DespesaTemporaria normalmente possui
             * apenas uma parcela por competência.
             *
             * Mesmo assim usamos distinct() para que a regra
             * permaneça segura caso haja dados inesperados.
             */
            val despesasTemporariasIds =
                parcelasAvulsas
                    .map {
                        it.despesaTemporariaId
                    }
                    .distinct()

            /*
             * =====================================================
             * 8. MARCA AS PARCELAS COMO PAGAS
             * =====================================================
             *
             * paga = true é um estado operacional.
             *
             * A autoridade histórica continua sendo:
             * fechamento.finalizado
             */
            if (parcelasAvulsas.isNotEmpty()) {

                despesaTemporariaParcelaRepository.marcarComoPagas(
                        fechamentoId
                    )
            }

            /*
             * =====================================================
             * 9. ENCERRA DESPESAS AVULSAS CONCLUÍDAS
             * =====================================================
             *
             * Somente encerramos o pai quando NÃO existir
             * nenhuma parcela paga = false.
             *
             * Isso inclui:
             *
             * - parcela futura ainda não associada;
             * - parcela futura já materializada em fechamento
             *   aberto.
             */
            despesasTemporariasIds
                .forEach { despesaTemporariaId ->

                    val possuiParcelaNaoPaga = despesaTemporariaParcelaRepository
                            .existeParcelaNaoPaga(
                                despesaTemporariaId
                            )

                    if (!possuiParcelaNaoPaga) {

                        despesaTemporariaRepository.encerrarDespesaTemporaria(
                                despesaTemporariaId
                            )
                    }
                }

            /*
             * =====================================================
             * 10. FINALIZA O FECHAMENTO
             * =====================================================
             *
             * Só chegamos aqui quando:
             *
             * - validações passaram;
             * - snapshots foram gravados;
             * - demonstrativos foram gerados;
             * - parcelas avulsas foram consolidadas;
             * - avulsas concluídas foram encerradas.
             */
            fechamentoRepository.update(
                fechamento.copy(
                    finalizado = true
                )
            )
        }
    }
}