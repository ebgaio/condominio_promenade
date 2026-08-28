package br.com.edificiopromenade.domain.repository

interface TransacaoRepository {

    suspend fun <T> executar(
        bloco: suspend () -> T
    ): T
}