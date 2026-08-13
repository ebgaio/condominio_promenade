package br.com.edificiopromenade.domain.usecase.tipodespesa

import br.com.edificiopromenade.domain.repository.TipoDespesaRepository
import jakarta.inject.Inject

class VerificarNomeReservadoTipoDespesaUseCase @Inject constructor(
    private val tipoDespesaRepository: TipoDespesaRepository
) {

    suspend operator fun invoke(
        nome: String
    ): Boolean {

        val nomeNormalizado =
            nome.trim()

        if (nomeNormalizado.isBlank()) {
            return false
        }

        /*
         * Procura no catálogo global de
         * tipos de despesa.
         *
         * Não importa se o tipo já foi usado
         * no fechamento atual ou não.
         */
        return tipoDespesaRepository
            .findAtivoByDescricao(
                nomeNormalizado
            ) != null
    }
}