package br.com.edificiopromenade.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "tipos_despesa"
)
data class TipoDespesaEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val descricao: String,

    val recorrente: Boolean,

    val ativo: Boolean = true
)