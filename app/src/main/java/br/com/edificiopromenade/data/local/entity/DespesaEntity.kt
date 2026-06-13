package br.com.edificiopromenade.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "despesas")
data class DespesaEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val fechamentoId: Long,

    val descricao: String,

    val valor: Double
)