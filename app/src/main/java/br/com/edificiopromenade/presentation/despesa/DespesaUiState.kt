package br.com.edificiopromenade.presentation.despesa

import br.com.edificiopromenade.presentation.common.message.UiMessage
import br.com.edificiopromenade.presentation.common.model.DespesaUi
import br.com.edificiopromenade.presentation.common.model.DespesaItemUi

data class DespesaUiState(

    val valor: String = "",

    val descricaoLivre: String = "",

    val despesas: List<DespesaUi> = emptyList(),

    val tiposDespesa: List<DespesaItemUi> = emptyList(),

    val tipoSelecionado: DespesaItemUi? = null,

    val expandirTipos: Boolean = false,

    val mensagem: UiMessage? = null,

    val despesaSelecionadaParaExclusao: DespesaUi? = null,

    val fechamentoFinalizado: Boolean = false
)