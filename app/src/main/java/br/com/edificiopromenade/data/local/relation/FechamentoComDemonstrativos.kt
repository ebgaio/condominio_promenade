package br.com.edificiopromenade.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import br.com.edificiopromenade.data.local.entity.DemonstrativoApartamentoEntity
import br.com.edificiopromenade.data.local.entity.FechamentoMensalEntity

data class FechamentoComDemonstrativos(

    @Embedded
    val fechamento: FechamentoMensalEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "fechamentoId"
    )
    val demonstrativos: List<DemonstrativoApartamentoEntity>
)