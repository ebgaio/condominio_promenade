package br.com.edificiopromenade.presentation.despesa

import br.com.edificiopromenade.data.local.entity.DespesaEntity
import br.com.edificiopromenade.data.local.entity.TipoDespesaEntity
import br.com.edificiopromenade.presentation.common.message.UiMessage

data class DespesaUiState(

    val valor: String = "",

    val despesas: List<DespesaEntity> = emptyList(),

    val tiposDespesa: List<TipoDespesaEntity> = emptyList(),

    val tipoSelecionado: TipoDespesaEntity? = null,

    val expandirTipos: Boolean = false,

    val mensagem: UiMessage? = null,

    val despesaSelecionadaParaExclusao: DespesaEntity? = null
)