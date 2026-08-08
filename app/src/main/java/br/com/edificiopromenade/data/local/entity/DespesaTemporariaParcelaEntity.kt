package br.com.edificiopromenade.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "despesas_temporarias_parcelas",

    foreignKeys = [

        ForeignKey(
            entity = DespesaTemporariaEntity::class,
            parentColumns = ["id"],
            childColumns = ["despesaTemporariaId"],
            onDelete = ForeignKey.CASCADE
        ),

        ForeignKey(
            entity = FechamentoMensalEntity::class,
            parentColumns = ["id"],
            childColumns = ["fechamentoId"],
            onDelete = ForeignKey.SET_NULL
        ),

        ForeignKey(
            entity = DespesaEntity::class,
            parentColumns = ["id"],
            childColumns = ["despesaId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],

    indices = [
        Index("despesaTemporariaId"),
        Index("fechamentoId"),
        Index("despesaId"),
        Index("competencia")
    ]
)
data class DespesaTemporariaParcelaEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val despesaTemporariaId: Long,

    /*
     * Formato AAAAMM.
     *
     * Exemplo:
     * 202609 = Setembro/2026
     */
    val competencia: Int,

    val numeroParcela: Int,

    val valor: Double,

    /*
     * Preenchido quando a parcela é associada
     * ao fechamento da respectiva competência.
     */
    val fechamentoId: Long? = null,

    /*
     * DespesaEntity criada para que a parcela
     * apareça na tela Despesas do Fechamento.
     */
    val despesaId: Long? = null,

    /*
     * Indica que a parcela já foi quitada
     * através do fechamento mensal.
     */
    val paga: Boolean = false
)