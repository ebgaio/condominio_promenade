package br.com.edificiopromenade.presentation.despesa.mapper

import br.com.edificiopromenade.data.local.entity.DespesaComTipoEntity
import br.com.edificiopromenade.presentation.despesa.model.DespesaUi

fun DespesaComTipoEntity.toUi(): DespesaUi {

    val avulsa =
        tipo.descricao.equals(
            "Despesa Avulsa",
            ignoreCase = true
        )

    return DespesaUi(

        id = despesa.id,

        descricao =
            if (avulsa) {
                despesa.descricaoLivre.ifBlank {
                    tipo.descricao
                }
            } else {

                /*
                 * Despesa fixa sempre utiliza
                 * o nome definido em TipoDespesa.
                 */
                tipo.descricao
            },

        valor = despesa.valor,

        avulsa =
            tipo.descricao.equals(
                "Despesa Avulsa",
                ignoreCase = true
            )
    )
}