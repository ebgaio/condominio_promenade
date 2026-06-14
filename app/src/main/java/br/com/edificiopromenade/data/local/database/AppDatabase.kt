package br.com.edificiopromenade.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.edificiopromenade.data.local.converter.DateConverters
import br.com.edificiopromenade.data.local.dao.ApartamentoDao
import br.com.edificiopromenade.data.local.dao.CondominioDao
import br.com.edificiopromenade.data.local.dao.DemonstrativoApartamentoDao
import br.com.edificiopromenade.data.local.dao.DespesaDao
import br.com.edificiopromenade.data.local.dao.FechamentoMensalDao
import br.com.edificiopromenade.data.local.dao.MoradorDao
import br.com.edificiopromenade.data.local.dao.TipoDespesaDao
import br.com.edificiopromenade.data.local.entity.ApartamentoEntity
import br.com.edificiopromenade.data.local.entity.CondominioEntity
import br.com.edificiopromenade.data.local.entity.ConfiguracaoEmailEntity
import br.com.edificiopromenade.data.local.entity.DemonstrativoApartamentoEntity
import br.com.edificiopromenade.data.local.entity.DespesaEntity
import br.com.edificiopromenade.data.local.entity.DespesaFechamentoEntity
import br.com.edificiopromenade.data.local.entity.FechamentoMensalEntity
import br.com.edificiopromenade.data.local.entity.HistoricoEnvioEmailEntity
import br.com.edificiopromenade.data.local.entity.MoradorEntity
import br.com.edificiopromenade.data.local.entity.TipoDespesaEntity

@Database(
    entities = [
        CondominioEntity::class,
        ApartamentoEntity::class,
        MoradorEntity::class,
        FechamentoMensalEntity::class,
        DespesaEntity::class,
        TipoDespesaEntity::class,
        DespesaFechamentoEntity::class,
        DemonstrativoApartamentoEntity::class,
        ConfiguracaoEmailEntity::class,
        HistoricoEnvioEmailEntity::class
                ],
    version = 5,
    exportSchema = true
)
@TypeConverters(
    DateConverters::class
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun apartamentoDao(): ApartamentoDao

    abstract fun moradorDao(): MoradorDao

    abstract fun tipoDespesaDao(): TipoDespesaDao

    abstract fun fechamentoMensalDao(): FechamentoMensalDao

    abstract fun condominioDao(): CondominioDao

    abstract fun despesaDao(): DespesaDao

    abstract fun demonstrativoDao(): DemonstrativoApartamentoDao
}