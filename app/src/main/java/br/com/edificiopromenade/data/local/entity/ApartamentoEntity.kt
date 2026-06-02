package br.com.edificiopromenade.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "apartamentos",

    foreignKeys = [
        ForeignKey(
            entity = CondominioEntity::class,
            parentColumns = ["id"],
            childColumns = ["condominioId"],
            onDelete = ForeignKey.CASCADE
        )
    ],

    indices = [
        Index("condominioId")
    ]
)
data class ApartamentoEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val condominioId: Long,

    val numero: String,

    val fracaoIdealAtual: Double,

    val ativo: Boolean = true
)