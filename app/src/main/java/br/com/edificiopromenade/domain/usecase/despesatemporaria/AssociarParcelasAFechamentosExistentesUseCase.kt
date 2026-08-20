package br.com.edificiopromenade.domain.usecase.despesatemporaria

import br.com.edificiopromenade.domain.repository.FechamentoRepository
import jakarta.inject.Inject

class AssociarParcelasAFechamentosExistentesUseCase @Inject constructor(
    private val fechamentoRepository: FechamentoRepository,
    private val associarParcelasAoFechamentoUseCase: AssociarParcelasAoFechamentoUseCase
) {

    suspend operator fun invoke(
        competencias: List<Int>
    ) {

        /*
         * Remove eventuais competências repetidas.
         *
         * Na estrutura atual normalmente haverá
         * uma parcela por competência, mas essa
         * proteção evita trabalho duplicado.
         */
        competencias
            .distinct()
            .forEach { competencia ->

                /*
                 * Competência está no formato:
                 *
                 * AAAAMM
                 *
                 * Exemplo:
                 *
                 * 202609
                 *
                 * ano = 2026
                 * mes = 9
                 */
                val ano = competencia / 100
                val mes = competencia % 100

                /*
                 * Verifica se o fechamento daquela
                 * competência já existe.
                 */
                val fechamento = fechamentoRepository.findByMesAno(
                        mes = mes,
                        ano = ano
                    )
                        ?: return@forEach

                /*
                 * Um fechamento finalizado é histórico.
                 *
                 * Nunca devemos inserir uma nova
                 * despesa nele.
                 */
                if (fechamento.finalizado) {
                    return@forEach
                }

                /*
                 * O fechamento existe e ainda está
                 * aberto.
                 *
                 * Materializa as parcelas pendentes
                 * da competência.
                 */
                associarParcelasAoFechamentoUseCase(
                    competencia = competencia,
                    fechamentoId = fechamento.id
                )
            }
    }
}