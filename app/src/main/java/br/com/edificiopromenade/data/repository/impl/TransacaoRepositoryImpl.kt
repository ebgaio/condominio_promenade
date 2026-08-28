package br.com.edificiopromenade.data.repository.impl

import androidx.room.withTransaction
import br.com.edificiopromenade.data.local.database.AppDatabase
import br.com.edificiopromenade.domain.repository.TransacaoRepository
import jakarta.inject.Inject

class TransacaoRepositoryImpl @Inject constructor(
    private val database: AppDatabase
) : TransacaoRepository {

    override suspend fun <T> executar(
        bloco: suspend () -> T
    ): T {

        return database.withTransaction {
            bloco()
        }
    }
}