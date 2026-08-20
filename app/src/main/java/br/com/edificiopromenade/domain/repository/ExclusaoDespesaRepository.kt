package br.com.edificiopromenade.domain.repository

interface ExclusaoDespesaRepository {

    suspend fun excluirRascunho(
        despesaId: Long
    )

    /*
     * Exclui somente a parcela correspondente
     * à DespesaEntity informada.
     */
    suspend fun excluirParcelaAvulsa(
        despesaId: Long
    )
}