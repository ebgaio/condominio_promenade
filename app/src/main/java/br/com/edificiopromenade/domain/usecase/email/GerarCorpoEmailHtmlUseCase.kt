package br.com.edificiopromenade.domain.usecase.email

import br.com.edificiopromenade.data.local.entity.DemonstrativoApartamentoEntity
import br.com.edificiopromenade.data.local.entity.DespesaEntity
import br.com.edificiopromenade.domain.email.EmailTemplateEngine
import br.com.edificiopromenade.presentation.util.formatarMoeda
import jakarta.inject.Inject

class GerarCorpoEmailHtmlUseCase @Inject constructor(
    private val templateEngine: EmailTemplateEngine
) {

    operator fun invoke(
        mesNome: String,
        ano: Int,
        despesas: List<DespesaEntity>,
        totalGeralDespesas: Double,
        demonstrativos: List<DemonstrativoApartamentoEntity>,
        totalGeralArrecadar: Double
    ): String {

        val tabelaDespesasHtml = buildString {
            despesas.forEach { despesa ->
                append("<tr><td>${despesa.descricaoLivre}</td><td>${formatarMoeda(despesa.valor)}</td></tr>")
            }
        }

        val listaBoletosHtml = buildString {
            demonstrativos.forEach { demo ->
                append("<li><strong>AP ${demo.numeroApartamentoHistorico} - ${demo.nomeMoradorHistorico}:</strong> ")
                append("Rateio Mensal: R$ ${formatarMoeda(demo.rateioMensal)} | ")
                append("Copasa: R$ ${formatarMoeda(demo.copasa)} | ")
                append("Fundo Reserva: R$ ${formatarMoeda(demo.fundoReserva)} | ")
                append("13º/FÉRIAS: R$ ${formatarMoeda(demo.decimoTerceiroFerias)} | ")
                append("<strong>Total: R$ ${formatarMoeda(demo.total)}</strong></li>")
            }
        }

        val template = templateEngine.carregarTemplate()

        return templateEngine.render(

            template,
            mapOf(
                "MES" to mesNome,
                "ANO" to ano.toString(),
                "TABELA_DESPESAS" to tabelaDespesasHtml.toString(),
                "LISTA_BOLETOS" to listaBoletosHtml.toString(),
                "TOTAL_DESPESAS" to formatarMoeda(totalGeralDespesas),
                "TOTAL_ARRECADAR" to formatarMoeda(totalGeralArrecadar)
            )
        )
    }
}
