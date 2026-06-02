package br.com.edificiopromenade.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "configuracao_email"
)
data class ConfiguracaoEmailEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val nomeAdministradora: String,

    val emailAdministradora: String,

    val assuntoPadrao: String,

    val textoIntroducao: String,

    val textoEncerramento: String,

    val ativo: Boolean = true
)