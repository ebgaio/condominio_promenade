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
             * Finalizar novamente não deve gerar
             * demonstrativos nem alterar histórico.
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
             * 4. CONFIRMA QUE EXISTEM DESPESAS
             * =====================================================
             */
            val despesas = despesaRepository
                .findListByFechamento(
                    fechamentoId
                )

            if (despesas.isEmpty()) {

                throw IllegalStateException(
                    "Não existem despesas cadastradas."
                )
            }

            /*
             * =====================================================
             * 5. GERA OS DEMONSTRATIVOS
             * =====================================================
             *
             * GerarDemonstrativosUseCase atualmente:
             *
             * - calcula os demonstrativos;
             * - exclui os antigos;
             * - insere os novos.
             *
             * Tudo isso agora ocorre dentro da mesma
             * transação aberta acima.
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
             * 6. LOCALIZA AS PARCELAS AVULSAS DESTE FECHAMENTO
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
             * 7. CONSOLIDA AS PARCELAS DO FECHAMENTO
             * =====================================================
             *
             * paga = true é um estado operacional.
             *
             * A autoridade histórica continua sendo:
             *
             * fechamento.finalizado
             */
            if (parcelasAvulsas.isNotEmpty()) {

                despesaTemporariaParcelaRepository.marcarComoPagas(
                        fechamentoId
                    )
            }

            /*
             * =====================================================
             * 8. ENCERRA DESPESAS AVULSAS CONCLUÍDAS
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
             * 9. SOMENTE AGORA FINALIZA O FECHAMENTO
             * =====================================================
             */
            fechamentoRepository.update(
                fechamento.copy(
                    finalizado = true
                )
            )
        }
    }
}