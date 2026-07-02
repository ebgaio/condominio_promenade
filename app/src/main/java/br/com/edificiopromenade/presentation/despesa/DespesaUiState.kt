package br.com.edificiopromenade.presentation.despesa

import br.com.edificiopromenade.presentation.common.message.UiMessage
import br.com.edificiopromenade.presentation.despesa.model.DespesaItemUi
import br.com.edificiopromenade.presentation.tipodespesa.model.TipoDespesaItemUi

data class DespesaUiState(

    val valor: String = "",

    val descricaoLivre: String = "",

    val despesas: List<DespesaItemUi> = emptyList(),

    val tiposDespesa: List<TipoDespesaItemUi> = emptyList(),

    val tipoSelecionado: TipoDespesaItemUi? = null,

    val expandirTipos: Boolean = false,

    val mensagem: UiMessage? = null,

    val despesaSelecionadaParaExclusao: DespesaItemUi? = null,

    val fechamentoFinalizado: Boolean = false
)