package br.com.edificiopromenade.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "condominios"
)
data class CondominioEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val nome: String,

    val cnpj: String? = null,

    val endereco: String? = null,

    val nomeAdministradora: String? = null,

    val emailAdministradora: String? = null,

    val ativo: Boolean = true,

    val dataCriacao: LocalDateTime,

    val dataInativacao: LocalDateTime? = null
)