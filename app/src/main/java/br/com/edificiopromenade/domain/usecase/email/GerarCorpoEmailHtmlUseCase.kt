package br.com.edificiopromenade.domain.usecase.email

import br.com.edificiopromenade.data.local.entity.DemonstrativoApartamentoEntity
import br.com.edificiopromenade.data.local.entity.DespesaEntity
import jakarta.inject.Inject

class GerarCorpoEmailHtmlUseCase @Inject constructor() {

    operator fun invoke(
        mesNome: String,
        ano: Int,
        despesas: List<DespesaEntity>,
        totalGeralDespesas: Double,
        demonstrativos: List<DemonstrativoApartamentoEntity>,
        totalGeralArrecadar: Double
    ): String {
        val tabelaDespesasHtml = StringBuilder()
        despesas.forEach { despesa ->
            tabelaDespesasHtml.append("<tr><td>${despesa.descricaoLivre}</td><td>${String.format("%.2f", despesa.valor)}</td></tr>")
        }

        val listaDemonstrativosHtml = StringBuilder()
        demonstrativos.forEach { demo ->
            listaDemonstrativosHtml.append("<li><strong>AP ${demo.numeroApartamentoHistorico} - ${demo.nomeMoradorHistorico}:</strong> ")
            listaDemonstrativosHtml.append("Rateio Mensal: R$ ${String.format("%.2f", demo.rateioMensal)} | ")
            listaDemonstrativosHtml.append("Copasa: R$ ${String.format("%.2f", demo.copasa)} | ")
            listaDemonstrativosHtml.append("Fundo Reserva: R$ ${String.format("%.2f", demo.fundoReserva)} | ")
            listaDemonstrativosHtml.append("13º/FÉRIAS: R$ ${String.format("%.2f", demo.decimoTerceiroFerias)} | ")
            listaDemonstrativosHtml.append("<strong>Total: R$ ${String.format("%.2f", demo.total)}</strong></li>")
        }

        val totalRateioTotal = demonstrativos.sumOf { it.rateioMensal }
        val totalCopasaTotal = demonstrativos.sumOf { it.copasa }
        val totalFundoReservaTotal = demonstrativos.sumOf { it.fundoReserva }
        val totalDecimoTotal = demonstrativos.sumOf { it.decimoTerceiroFerias }

        return """
            <h3>Relatório de Despesas - Condomínio Edifício Promenade - Referência: $mesNome/$ano</h3>

            <table border="1" cellpadding="5" style="border-collapse: collapse;">
              <thead>
                <tr style="background-color: #f2f2f2;">
                  <th>Item</th>
                  <th>Valor (R$)</th>
                </tr>
              </thead>
              <tbody>
                $tabelaDespesasHtml
                <tr style="font-weight: bold;"><td>TOTAL GERAL</td><td>${String.format("%.2f", totalGeralDespesas)}</td></tr>
              </tbody>
            </table>

            <h3>Dados para Confecção dos Boletos</h3>
            <p>Prezada Administradora, favor emitir os boletos com os seguintes desmembramentos:</p>

            <ul>
              $listaDemonstrativosHtml
            </ul>

            <p><strong>Resumo Geral dos Boletos (Conferência):</strong><br>
            Rateio Mensal Total: R$ ${String.format("%.2f", totalRateioTotal)}<br>
            Copasa Total: R$ ${String.format("%.2f", totalCopasaTotal)}<br>
            Fundo de Reserva Total: R$ ${String.format("%.2f", totalFundoReservaTotal)}<br>
            13º/Férias Total: R$ ${String.format("%.2f", totalDecimoTotal)}<br>
            <strong>TOTAL GERAL A ARRECADAR: R$ ${String.format("%.2f", totalGeralArrecadar)}</strong></p>
        """.trimIndent()
    }
}
