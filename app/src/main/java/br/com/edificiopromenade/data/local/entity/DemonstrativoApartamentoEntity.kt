package br.com.edificiopromenade.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "demonstrativos_apartamento",

    foreignKeys = [

        ForeignKey(
            entity = FechamentoMensalEntity::class,
            parentColumns = ["id"],
            childColumns = ["fechamentoId"],
            onDelete = ForeignKey.CASCADE
        ),

        ForeignKey(
            entity = ApartamentoEntity::class,
            parentColumns = ["id"],
            childColumns = ["apartamentoId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],

    indices = [
        Index("fechamentoId"),
        Index("apartamentoId")
    ]
)
data class DemonstrativoApartamentoEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val fechamentoId: Long,

    val apartamentoId: Long,

    val nomeMoradorHistorico: String,

    val percentualCopasaHistorica: Double,

    val rateioMensal: Double,

    val copasa: Double,

    val fundoReserva: Double,

    val decimoTerceiroFerias: Double,

    val total: Double
)