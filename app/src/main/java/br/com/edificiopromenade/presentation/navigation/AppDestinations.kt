package br.com.edificiopromenade.presentation.navigation

object AppDestinations {

    const val HOME = "home"

    const val MORADORES =
        "moradores?modoInicializacao={modoInicializacao}"

    const val HISTORICO = "historico"

    const val NOVO_FECHAMENTO = "novo_fechamento"

    const val CONFIGURACOES = "configuracoes"

    const val CONDOMINIO =
        "condominio?modoInicializacao={modoInicializacao}"

    const val APARTAMENTOS =
        "apartamentos?modoInicializacao={modoInicializacao}"

    const val APARTAMENTO_DETALHE =
        "apartamento_detalhe/{id}"

    const val DESPESAS =
        "despesas/{fechamentoId}"

    const val DEMONSTRATIVOS =
        "demonstrativos/{fechamentoId}"

    const val DESPESA_ITEM =
        "despesa_item/{despesaId}"

    const val SPLASH = "splash"

    const val INITIALIZATION = "initialization"

    const val DESPESA_TEMPORARIA =
        "despesa_temporaria/{fechamentoId}"


    /*
     * ============================================================
     * ROTAS DE NAVEGAÇÃO
     * ============================================================
     */

    fun condominioRoute(
        modoInicializacao: Boolean
    ): String {

        return "condominio?modoInicializacao=$modoInicializacao"
    }


    fun apartamentosRoute(
        modoInicializacao: Boolean
    ): String {

        return "apartamentos?modoInicializacao=$modoInicializacao"
    }


    fun moradoresRoute(
        modoInicializacao: Boolean
    ): String {

        return "moradores?modoInicializacao=$modoInicializacao"
    }


    /*
     * ============================================================
     * ROTAS DE FECHAMENTO
     * ============================================================
     */

    fun despesasRoute(
        fechamentoId: Long
    ): String {

        return "despesas/$fechamentoId"
    }


    fun demonstrativosRoute(
        fechamentoId: Long
    ): String {

        return "demonstrativos/$fechamentoId"
    }


    fun despesaItemRoute(
        despesaId: Long
    ): String {

        return "despesa_item/$despesaId"
    }


    fun despesaTemporariaRoute(
        fechamentoId: Long
    ): String {
        return "despesa_temporaria/$fechamentoId"
    }

    /*
     * ============================================================
     * ROTA DE DETALHE DO APARTAMENTO
     * ============================================================
     */

    fun apartamentoDetalheRoute(
        apartamentoId: Long
    ): String {

        return "apartamento_detalhe/$apartamentoId"
    }
}
