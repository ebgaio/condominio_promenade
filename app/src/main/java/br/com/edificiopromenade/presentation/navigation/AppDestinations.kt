package br.com.edificiopromenade.presentation.navigation

object AppDestinations {

    const val HOME = "home"

    const val MORADORES = "moradores"

    const val HISTORICO = "historico"

    const val NOVO_FECHAMENTO = "novo_fechamento"

    const val CONFIGURACOES = "configuracoes"

    const val CONDOMINIO = "condominio"

    const val APARTAMENTOS = "apartamentos"

    const val APARTAMENTO_DETALHE = "apartamento_detalhe/{id}"

    const val DESPESAS = "despesas/{fechamentoId}"

    const val DEMONSTRATIVOS = "demonstrativos/{fechamentoId}"

    const val DESPESA_ITEM = "despesa_item/{despesaId}"

    fun despesaItemRoute(
        despesaId: Long
    ) = "despesa_item/$despesaId"

    fun despesasRoute(
        fechamentoId: Long
    ) = "despesas/$fechamentoId"

    fun demonstrativosRoute(
        fechamentoId: Long
    ) = "demonstrativos/$fechamentoId"
}