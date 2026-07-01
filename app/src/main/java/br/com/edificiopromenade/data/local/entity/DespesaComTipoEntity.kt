package br.com.edificiopromenade.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class DespesaComTipoEntity(

    @Embedded
    val despesa: DespesaEntity,

    @Relation(
        parentColumn = "tipoDespesaId",
        entityColumn = "id"
    )
    val tipo: TipoDespesaEntity
)