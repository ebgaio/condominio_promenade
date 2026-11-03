package br.com.edificiopromenade.domain.mapper

import br.com.edificiopromenade.data.local.entity.DespesaComTipoEntity
import br.com.edificiopromenade.domain.model.DespesaCalculavel

fun DespesaComTipoEntity.toModel():
    DespesaCalculavel {

    val avulsa =
        tipo.descricao.equals(
            "Despesa Avulsa",
            ignoreCase = true
    )

    /*
     * Enquanto o fechamento ainda estiver aberto,
     * os campos históricos permanecem null.
     *
     * Nesse caso utilizamos os dados operacionais.
     */
    val descricaoAtual =
        if (avulsa) {

            despesa.descricaoLivre.ifBlank {
                tipo.descricao
            }

        } else {

            tipo.descricao
        }

    return DespesaCalculavel(

        id = despesa.id,

        /*
         * Se já existe snapshot histórico,
         * ele possui prioridade.
         */
        descricao =
            despesa.descricaoHistorica
                ?: descricaoAtual,

        valor = despesa.valor,

        /*
         * Fechamento aberto:
         * usa TipoDespesa atual.
         *
         * Fechamento consolidado:
         * usa a regra congelada.
         */
        usaFracaoIdeal =
            despesa.usaFracaoIdealHistorica
                ?: tipo.usaFracaoIdeal
    )
}
