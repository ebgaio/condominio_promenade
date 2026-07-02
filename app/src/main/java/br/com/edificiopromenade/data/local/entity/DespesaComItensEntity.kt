package br.com.edificiopromenade.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class DespesaComItensEntity(

    @Embedded
    val despesa: DespesaEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "despesaId"
    )
    val itens: List<DespesaItemEntity>
)