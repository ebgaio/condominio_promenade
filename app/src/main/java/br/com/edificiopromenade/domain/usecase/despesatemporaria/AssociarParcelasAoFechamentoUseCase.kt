package br.com.edificiopromenade.domain.usecase.despesatemporaria

import br.com.edificiopromenade.data.local.entity.DespesaEntity
import br.com.edificiopromenade.domain.repository.DespesaRepository
import br.com.edificiopromenade.domain.repository.DespesaTemporariaParcelaRepository
import br.com.edificiopromenade.domain.repository.DespesaTemporariaRepository
import br.com.edificiopromenade.domain.repository.TipoDespesaRepository
import jakarta.inject.Inject

class AssociarParcelasAoFechamentoUseCase
@Inject constructor(
    private val parcelaRepository: DespesaTemporariaParcelaRepository,
    private val despesaTemporariaRepository: DespesaTemporariaRepository,
    private val despesaRepository: DespesaRepository,
    private val tipoDespesaRepository: TipoDespesaRepository
) {

    suspend operator fun invoke(
        fechamentoId: Long,
        competencia: Int
    ) {

        val parcelas =
            parcelaRepository.buscarParcelasDaCompetencia(
                competencia
            )

        if (parcelas.isEmpty()) {
            return
        }

        val tipoDespesaAvulsa =
            tipoDespesaRepository.findAtivoByDescricao(
                "Despesa Avulsa"
            )
                ?: throw IllegalStateException(
                    "O tipo técnico 'Despesa Avulsa' não foi encontrado."
                )

        parcelas.forEach { parcela ->

            val despesaTemporaria =
                despesaTemporariaRepository.findById(
                    parcela.despesaTemporariaId
                )
                    ?: return@forEach

            val descricao =
                if (
                    despesaTemporaria.quantidadeParcelas > 1
                ) {
                    "${despesaTemporaria.descricao} - " +
                            "Parcela ${parcela.numeroParcela}/" +
                            "${despesaTemporaria.quantidadeParcelas}"
                } else {
                    despesaTemporaria.descricao
                }

            val despesaId =
                despesaRepository.insert(
                    DespesaEntity(
                        fechamentoId = fechamentoId,
                        tipoDespesaId = tipoDespesaAvulsa.id,
                        descricaoLivre = descricao,
                        valor = parcela.valor
                    )
                )

            val associada =
                parcelaRepository
                    .associarAoFechamentoEDespesa(
                        parcelaId = parcela.id,
                        fechamentoId = fechamentoId,
                        despesaId = despesaId
                    )

            if (!associada) {
                /*
                 * A parcela não foi associada, provavelmente
                 * porque já havia sido processada.
                 *
                 * Remove a DespesaEntity criada para não
                 * deixar um registro duplicado.
                 */
                despesaRepository.deleteById(
                    despesaId
                )

                return@forEach
            }

            val existeParcelaPendente =
                parcelaRepository.existeParcelaPendente(
                    parcela.despesaTemporariaId
                )

            if (!existeParcelaPendente) {
                despesaTemporariaRepository
                    .encerrarDespesaTemporaria(
                        parcela.despesaTemporariaId
                    )
            }
        }
    }
}