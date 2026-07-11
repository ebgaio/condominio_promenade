package br.com.edificiopromenade.domain.usecase.historico

import br.com.edificiopromenade.presentation.history.model.DemonstrativoHistoricoUi
import br.com.edificiopromenade.presentation.util.formatarMoeda
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

            • Rateio Mensal: R$ ${formatarMoeda(demonstrativo.rateioMensal)}
            • Copasa (Água): R$ ${formatarMoeda(demonstrativo.copasa)}
            • Fundo de Reserva: R$ ${formatarMoeda(demonstrativo.fundoReserva)}
            • 13º/Férias: R$ ${formatarMoeda(demonstrativo.decimoTerceiroFerias)}

            *Valor Total do Boleto: R$ ${formatarMoeda(demonstrativo.total)}*
        """.trimIndent()
    }
}
