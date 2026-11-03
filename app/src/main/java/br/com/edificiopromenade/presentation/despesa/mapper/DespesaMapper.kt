package br.com.edificiopromenade.presentation.despesa.mapper

import br.com.edificiopromenade.data.local.entity.DespesaComTipoEntity
import br.com.edificiopromenade.presentation.despesa.model.DespesaUi

fun DespesaComTipoEntity.toUi():
    DespesaUi {

    val avulsa =
        tipo.descricao.equals(
            "Despesa Avulsa",
            ignoreCase = true
        )

    val descricaoAtual =
        if (avulsa) {

            despesa.descricaoLivre.ifBlank {
                tipo.descricao
            }

        } else {

            /*
             * Enquanto o fechamento estiver aberto,
             * despesa fixa acompanha o catálogo.
             */
            tipo.descricao
        }

    return DespesaUi(

        id = despesa.id,

        /*
         * Após a consolidação, não dependemos mais
         * do nome atual existente em TipoDespesa.
         */
        descricao =
            despesa.descricaoHistorica
               ?: descricaoAtual,

        valor = despesa.valor,

        avulsa = avulsa
    )
}