package br.com.edificiopromenade.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import br.com.edificiopromenade.data.local.entity.DespesaFechamentoEntity
import br.com.edificiopromenade.data.local.entity.FechamentoMensalEntity

data class FechamentoComDespesas(

    @Embedded
    val fechamento: FechamentoMensalEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "fechamentoId"
    )
    val despesas: List<DespesaFechamentoEntity>
)