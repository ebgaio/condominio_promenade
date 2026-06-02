package br.com.edificiopromenade.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "moradores",

    foreignKeys = [
        ForeignKey(
            entity = ApartamentoEntity::class,
            parentColumns = ["id"],
            childColumns = ["apartamentoId"],
            onDelete = ForeignKey.CASCADE
        )
    ],

    indices = [
        Index("apartamentoId")
    ]
)
data class MoradorEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val apartamentoId: Long,

    val nome: String,

    val dataInicio: LocalDate,

    val dataFim: LocalDate?,

    val ativo: Boolean = true
)