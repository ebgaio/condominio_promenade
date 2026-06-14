package br.com.edificiopromenade.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "fechamentos_mensais",

    indices = [
        Index(
            value = ["mes", "ano"],
            unique = true
        )
    ]
)
data class FechamentoMensalEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val mes: Int,

    val ano: Int,

    val dataCriacao: LocalDateTime,

    val valorFundoReserva: Double = 0.0,

    val valorDecimoTerceiroFerias: Double = 0.0,

    val finalizado: Boolean = false
)