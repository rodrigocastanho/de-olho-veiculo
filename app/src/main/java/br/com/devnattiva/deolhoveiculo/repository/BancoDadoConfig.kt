package br.com.devnattiva.deolhoveiculo.repository

import android.arch.persistence.room.*
import android.content.Context
import br.com.devnattiva.deolhoveiculo.configuration.ConversorData
import br.com.devnattiva.deolhoveiculo.model.Veiculo
import br.com.devnattiva.deolhoveiculo.model.Manutencao

@Database(entities = [Veiculo::class, Manutencao::class], version = 1, exportSchema = false)
@TypeConverters(ConversorData::class)
abstract class BancoDadoConfig : RoomDatabase() {
    abstract fun controleDAO(): ControleDAO

    companion object{

         fun  getInstance(context: Context): BancoDadoConfig {

            return Room.databaseBuilder(context.applicationContext, BancoDadoConfig::class.java,"Mveiculo").build()

         }

    }

}