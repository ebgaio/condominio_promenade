package br.com.edificiopromenade.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "despesa_itens",

    foreignKeys = [

        ForeignKey(
            entity = DespesaEntity::class,
            parentColumns = ["id"],
            childColumns = ["despesaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],

    indices = [
        Index("despesaId")
    ]
)
data class DespesaItemEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val despesaId: Long,

    /**
     * Opcional.
     * Exemplo:
     * "Conta Janeiro"
     * "Área Comum"
     * "Troca de Lâmpadas"
     */
    val descricao: String = "",

    val valor: Double
)