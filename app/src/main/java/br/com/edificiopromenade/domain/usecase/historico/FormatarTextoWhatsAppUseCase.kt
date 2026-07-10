package br.com.edificiopromenade.domain.usecase.historico

import br.com.edificiopromenade.presentation.history.model.DemonstrativoHistoricoUi
import jakarta.inject.Inject

class FormatarTextoWhatsAppUseCase @Inject constructor() {

    operator fun invoke(
        demonstrativo: DemonstrativoHistoricoUi,
        mesNome: String,
        ano: Int
    ): String {
        return """
            *Condomínio Edifício Promenade* 🏢
            *Resumo de Cobrança - Referência: $mesNome/$ano*

            Olá, segue o detalhamento dos valores para a confecção do seu boleto deste mês:

            • *Unidade:* ${demonstrativo.numeroApartamento}
            • *Inquilino:* ${demonstrativo.nomeMorador}

            • Rateio Mensal: R$ ${String.format("%.2f", demonstrativo.rateioMensal)}
            • Copasa (Água): R$ ${String.format("%.2f", demonstrativo.copasa)}
            • Fundo de Reserva: R$ ${String.format("%.2f", demonstrativo.fundoReserva)}
            • 13º/Férias: R$ ${String.format("%.2f", demonstrativo.decimoTerceiroFerias)}

            *Valor Total do Boleto: R$ ${String.format("%.2f", demonstrativo.total)}*
        """.trimIndent()
    }
}
