package br.com.edificiopromenade.di

import android.content.Context
import androidx.room.Room
import br.com.edificiopromenade.data.local.dao.ApartamentoDao
import br.com.edificiopromenade.data.local.dao.CondominioDao
import br.com.edificiopromenade.data.local.dao.DespesaDao
import br.com.edificiopromenade.data.local.dao.FechamentoMensalDao
import br.com.edificiopromenade.data.local.dao.MoradorDao
import br.com.edificiopromenade.data.local.dao.TipoDespesaDao
import br.com.edificiopromenade.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {

        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "edificio_promenade.db"
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    fun provideApartamentoDao(
        database: AppDatabase
    ): ApartamentoDao =
        database.apartamentoDao()

    @Provides
    fun provideMoradorDao(
        database: AppDatabase
    ): MoradorDao =
        database.moradorDao()

    @Provides
    fun provideTipoDespesaDao(
        database: AppDatabase
    ): TipoDespesaDao =
        database.tipoDespesaDao()

    @Provides
    fun provideFechamentoMensalDao(
        database: AppDatabase
    ): FechamentoMensalDao =
        database.fechamentoMensalDao()

    @Provides
    fun provideCondominioDao(
        database: AppDatabase
    ): CondominioDao =
        database.condominioDao()

    @Provides
    fun despesaDao(
        database: AppDatabase
    ): DespesaDao =
        database.despesaDao()
}