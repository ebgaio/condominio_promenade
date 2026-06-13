package br.com.edificiopromenade.di

import br.com.edificiopromenade.data.repository.impl.ApartamentoRepositoryImpl
import br.com.edificiopromenade.data.repository.impl.CondominioRepositoryImpl
import br.com.edificiopromenade.data.repository.impl.DespesaRepositoryImpl
import br.com.edificiopromenade.data.repository.impl.FechamentoRepositoryImpl
import br.com.edificiopromenade.data.repository.impl.MoradorRepositoryImpl
import br.com.edificiopromenade.domain.repository.ApartamentoRepository
import br.com.edificiopromenade.domain.repository.CondominioRepository
import br.com.edificiopromenade.domain.repository.DespesaRepository
import br.com.edificiopromenade.domain.repository.FechamentoRepository
import br.com.edificiopromenade.domain.repository.MoradorRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindApartamentoRepository(
        impl: ApartamentoRepositoryImpl
    ): ApartamentoRepository

    @Binds
    @Singleton
    abstract fun bindMoradorRepository(
        impl: MoradorRepositoryImpl
    ): MoradorRepository

    @Binds
    @Singleton
    abstract fun bindFechamentoRepository(
        impl: FechamentoRepositoryImpl
    ): FechamentoRepository

    @Binds
    @Singleton
    abstract fun bindCondominioRepository(
        impl: CondominioRepositoryImpl
    ): CondominioRepository

    @Binds
    @Singleton
    abstract fun bindDespesaRepository(
        impl: DespesaRepositoryImpl
    ): DespesaRepository
}