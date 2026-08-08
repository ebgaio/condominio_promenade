package br.com.edificiopromenade.presentation.despesa.mapper

import br.com.edificiopromenade.data.local.entity.DespesaComTipoEntity
import br.com.edificiopromenade.presentation.despesa.model.DespesaUi

fun DespesaComTipoEntity.toUi() =

    DespesaUi(

        id = despesa.id,

        descricao =
            despesa.descricaoLivre.ifBlank {
                tipo.descricao
            },

        valor = despesa.valor
    )