package br.com.edificiopromenade.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import br.com.edificiopromenade.data.local.entity.ApartamentoEntity
import br.com.edificiopromenade.data.local.entity.DemonstrativoApartamentoEntity

data class ApartamentoComHistorico(

    @Embedded
    val apartamento: ApartamentoEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "apartamentoId"
    )
    val historico: List<DemonstrativoApartamentoEntity>
)