package br.com.edificiopromenade.presentation.despesatemporaria

import br.com.edificiopromenade.presentation.common.message.UiMessage

data class DespesaTemporariaUiState(

    val descricao: String = "",
    val quantidadeParcelas: String = "1",
    val parcelas: List<String> = listOf(""),

    /*
     * Primeira competência das parcelas.
     */
    val mesInicial: String = "",
    val anoInicial: String = "",

    val meses: List<String> = listOf(
        "01",
        "02",
        "03",
        "04",
        "05",
        "06",
        "07",
        "08",
        "09",
        "10",
        "11",
        "12"
    ),

    val expandirMeses: Boolean = false,
    val carregando: Boolean = false,
    val mensagem: UiMessage? = null,

)