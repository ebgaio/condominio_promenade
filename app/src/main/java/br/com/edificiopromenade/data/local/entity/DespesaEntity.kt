package br.com.edificiopromenade.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "despesas",

    foreignKeys = [

        ForeignKey(
            entity = FechamentoMensalEntity::class,
            parentColumns = ["id"],
            childColumns = ["fechamentoId"],
            onDelete = ForeignKey.CASCADE
        ),

        ForeignKey(
            entity = TipoDespesaEntity::class,
            parentColumns = ["id"],
            childColumns = ["tipoDespesaId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],

    indices = [
        Index("fechamentoId"),
        Index("tipoDespesaId")
    ]
)
data class DespesaEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val fechamentoId: Long,

    val tipoDespesaId: Long,

    val descricaoLivre: String,

    // Total calculado automaticamente pela soma dos itens
    val valor: Double = 0.0,

    /*
     * Preenchido somente quando o fechamento
     * se torna definitivo.
     *
     * Enquanto o fechamento estiver aberto,
     * permanece null e a aplicação continua
     * utilizando os dados atuais do TipoDespesa.
     */
    val descricaoHistorica: String? = null,

    /*
     * Regra de rateio utilizada no instante
     * da finalização.
     *
     * null = fechamento ainda não consolidado.
     */
    val usaFracaoIdealHistorica: Boolean? = null
)