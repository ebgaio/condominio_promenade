package br.com.edificiopromenade.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "despesas_temporarias"
)
data class DespesaTemporariaEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    /*
     * Descrição apresentada ao usuário.
     *
     * Ex:
     * Pintura Fachada
     * Reforma Elevador
     * Troca da Bomba
     */
    val descricao: String,

    /**
     * Número total de parcelas.
     */
    val quantidadeParcelas: Int,

    /**
     * Controla se ainda existem parcelas pendentes.
     */
    val ativa: Boolean = true
)