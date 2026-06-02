package br.com.edificiopromenade.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "despesas_fechamento",

    foreignKeys = [

        ForeignKey(
            entity = FechamentoMensalEntity::class,
            parentColumns = ["id"],
            childColumns = ["fechamentoId"],
            onDelete = ForeignKey.CASCADE
        ),

        ForeignKey(
            entity = TipoDespesaEntity::class,
            parentColumns = ["id"],
            childColumns = ["tipoDespesaId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],

    indices = [
        Index("fechamentoId"),
        Index("tipoDespesaId")
    ]
)
data class DespesaFechamentoEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val fechamentoId: Long,

    val tipoDespesaId: Long,

    val descricaoHistorica: String,

    val valor: Double?
)