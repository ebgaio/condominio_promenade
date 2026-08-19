package br.com.edificiopromenade.domain.repository

interface ExclusaoDespesaRepository {

    suspend fun excluirRascunho(
        despesaId: Long
    )
}