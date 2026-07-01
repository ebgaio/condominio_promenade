package br.com.edificiopromenade.presentation.despesa

import br.com.edificiopromenade.presentation.common.message.UiMessage
import br.com.edificiopromenade.presentation.despesa.model.DespesaItemUi
import br.com.edificiopromenade.presentation.despesa.model.TipoDespesaUi

data class DespesaUiState(

    val valor: String = "",

    val descricaoLivre: String = "",

    val despesas: List<DespesaItemUi> = emptyList(),

    val tiposDespesa: List<TipoDespesaUi> = emptyList(),

    val tipoSelecionado: TipoDespesaUi? = null,

    val expandirTipos: Boolean = false,

    val mensagem: UiMessage? = null,

    val despesaSelecionadaParaExclusao: DespesaItemUi? = null,

    val fechamentoFinalizado: Boolean = false
)