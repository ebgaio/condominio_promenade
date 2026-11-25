package br.com.edificiopromenade.domain.usecase.despesa

import br.com.edificiopromenade.domain.model.AtualizarDespesaCommand
import br.com.edificiopromenade.domain.repository.DespesaRepository
import br.com.edificiopromenade.domain.repository.DespesaTemporariaParcelaRepository
import br.com.edificiopromenade.domain.repository.DespesaTemporariaRepository
import br.com.edificiopromenade.domain.repository.FechamentoRepository
import br.com.edificiopromenade.domain.repository.TransacaoRepository
import jakarta.inject.Inject

class AtualizarDespesaUseCase @Inject constructor(

    private val despesaRepository:DespesaRepository,
    private val fechamentoRepository:FechamentoRepository,
    private val despesaTemporariaRepository:DespesaTemporariaRepository,
    private val despesaTemporariaParcelaRepository:DespesaTemporariaParcelaRepository,
    private val transacaoRepository:TransacaoRepository
) {
    suspend operator fun invoke(
        command: AtualizarDespesaCommand
    ) {

        transacaoRepository.executar {

            /*
             * =====================================================
             * 1. LOCALIZA A DESPESA
             * =====================================================
             */
            val despesa =
                despesaRepository.findById(
                    command.despesaId
                )
                    ?: throw IllegalStateException(
                        "Despesa não encontrada."
                    )

            /*
             * =====================================================
             * 2. PROTEGE FECHAMENTO FINALIZADO
             * =====================================================
             *
             * A ViewModel pode possuir um estado antigo.
             * A autoridade é sempre o banco.
             */
            val fechamento =
                fechamentoRepository.findById(
                    despesa.fechamentoId
                )
                    ?: throw IllegalStateException(
                        "Fechamento da despesa não encontrado."
                    )

            if (fechamento.finalizado) {

                throw IllegalStateException(
                    "Não é permitido alterar uma despesa " +
                        "de um fechamento finalizado."
                )
            }

            /*
             * =====================================================
             * 3. VALIDA O VALOR
             * =====================================================
             */
            if (command.valor <= 0.0) {

                throw IllegalStateException(
                    "Informe um valor válido para a despesa."
                )
            }

            /*
             * =====================================================
             * 4. PROTEGE DESPESAS QUE POSSUEM ITENS
             * =====================================================
             *
             * Quando há itens, o valor da despesa é derivado
             * exclusivamente da soma deles.
             */
            val despesaComItens =
                despesaRepository.findComItens(
                    despesa.id
                )

            if (
                despesaComItens != null &&
                despesaComItens.itens.isNotEmpty()
            ) {

                throw IllegalStateException(
                    "Esta despesa possui itens. " +
                        "Altere os valores pela tela Itens."
                )
            }

            /*
             * =====================================================
             * 5. DESCOBRE SE É UMA DESPESA AVULSA
             * =====================================================
             *
             * Não confiamos em um Boolean vindo da tela.
             *
             * Despesa avulsa possui relacionamento com
             * despesas_temporarias_parcelas.
             */
            val parcelaAtual =
                despesaTemporariaParcelaRepository
                    .findByDespesaId(
                        despesa.id
                    )

            /*
             * =====================================================
             * DESPESA FIXA
             * =====================================================
             */
            if (parcelaAtual == null) {

                /*
                 * Nome de despesa fixa continua vindo
                 * exclusivamente de TipoDespesaEntity.
                 */
                despesaRepository.update(
                    despesa.copy(
                        descricaoLivre = "",
                        valor = command.valor
                    )
                )

                return@executar
            }

            /*
             * =====================================================
             * 6. DESPESA AVULSA
             * =====================================================
             */
            val novoNome = command.descricao.trim()

            if (novoNome.isBlank()) {
                throw IllegalStateException(
                    "Informe o nome da despesa avulsa."
                )
            }

            /*
             * =====================================================
             * 7. LOCALIZA O PAI DA DESPESA AVULSA
             * =====================================================
             */
            val despesaTemporaria =
                despesaTemporariaRepository.findById(
                    parcelaAtual.despesaTemporariaId
                )
                    ?: throw IllegalStateException(
                        "Despesa avulsa original não encontrada."
                    )

            /*
             * =====================================================
             * 8. LOCALIZA TODAS AS PARCELAS
             * =====================================================
             *
             * Precisamos delas para propagar eventual alteração
             * do nome-base às parcelas ainda não históricas.
             */
            val parcelas =
                despesaTemporariaParcelaRepository
                    .buscarPorDespesaTemporariaId(
                        despesaTemporaria.id
                    )

            /*
             * =====================================================
             * 9. ATUALIZA O NOME-BASE
             * =====================================================
             *
             * Parcelas futuras ainda não materializadas utilizarão
             * este nome quando forem transformadas em DespesaEntity.
             */
            despesaTemporariaRepository.update(
                despesaTemporaria.copy(
                    descricao = novoNome
                )
            )

            /*
             * =====================================================
             * 10. PROPAGA O NOME ÀS PARCELAS MATERIALIZADAS
             *     QUE AINDA NÃO SÃO HISTÓRICAS
             * =====================================================
             */
            parcelas.forEach { parcela ->

                val despesaParcelaId =
                    parcela.despesaId
                        ?: return@forEach

                /*
                 * Se a parcela pertence a um fechamento,
                 * verificamos sua autoridade histórica.
                 */
                val fechamentoParcelaId = parcela.fechamentoId

                if (fechamentoParcelaId != null) {

                    val fechamentoParcela =
                        fechamentoRepository.findById(
                            fechamentoParcelaId
                        )
                            ?: throw IllegalStateException(
                                "Fechamento da parcela " +
                                    "${parcela.numeroParcela} " +
                                    "não encontrado."
                            )

                    /*
                     * Histórico finalizado é intocável.
                     */
                    if (fechamentoParcela.finalizado) {
                        return@forEach
                    }
                }

                val despesaParcela =
                    despesaRepository.findById(
                        despesaParcelaId
                    )
                        ?: throw IllegalStateException(
                            "Despesa da parcela " +
                                "${parcela.numeroParcela} " +
                                "não encontrada."
                        )

                val novaDescricao =
                    if (
                        despesaTemporaria.quantidadeParcelas > 1
                    ) {

                        "$novoNome - Parcela " +
                            "${parcela.numeroParcela}/" +
                            "${despesaTemporaria.quantidadeParcelas}"

                    } else {

                        novoNome
                    }

                /*
                 * Somente a parcela atualmente editada
                 * recebe também o novo valor.
                 *
                 * As demais recebem apenas o novo nome.
                 */
                val despesaAtualizada =
                    if (
                        despesaParcela.id ==
                        despesa.id
                    ) {

                        despesaParcela.copy(
                            descricaoLivre =
                                novaDescricao,
                            valor =
                                command.valor
                        )

                    } else {

                        despesaParcela.copy(
                            descricaoLivre =
                                novaDescricao
                        )
                    }

                despesaRepository.update(
                    despesaAtualizada
                )
            }

            /*
             * =====================================================
             * 11. SINCRONIZA O VALOR DA PARCELA OPERACIONAL
             * =====================================================
             *
             * Somente a parcela efetivamente editada recebe
             * o novo valor.
             */
            despesaTemporariaParcelaRepository
                .atualizarValorPorDespesaId(
                    despesaId =
                        despesa.id,
                    valor =
                        command.valor
                )
        }
    }
}