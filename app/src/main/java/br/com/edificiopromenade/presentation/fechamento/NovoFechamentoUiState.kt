package br.com.edificiopromenade.presentation.fechamento

import br.com.edificiopromenade.data.local.entity.FechamentoMensalEntity
import br.com.edificiopromenade.presentation.common.message.UiMessage

data class NovoFechamentoUiState(

    val mes: String = "",

    val ano: String = "",

    val fundoReserva: String = "",

    val decimoTerceiroFerias: String = "",

    val carregando: Boolean = false,

    val mensagem: UiMessage? = null,

    val fechamentos: List<FechamentoMensalEntity> = emptyList(),

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

    val expandirMeses: Boolean = false
)