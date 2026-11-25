package br.com.edificiopromenade.domain.model

data class AtualizarDespesaCommand(

    val despesaId: Long,

    /*
     * Utilizado somente para despesa avulsa.
     * Para despesa fixa pode permanecer vazio.
     */
    val descricao: String,

    val valor: Double
)