package br.com.edificiopromenade.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "historico_envio_email",

    foreignKeys = [
        ForeignKey(
            entity = FechamentoMensalEntity::class,
            parentColumns = ["id"],
            childColumns = ["fechamentoId"],
            onDelete = ForeignKey.CASCADE
        )
    ],

    indices = [
        Index("fechamentoId")
    ]
)
data class HistoricoEnvioEmailEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val fechamentoId: Long,

    val emailDestino: String,

    val dataEnvio: LocalDateTime,

    val sucesso: Boolean,

    val mensagemRetorno: String?
)