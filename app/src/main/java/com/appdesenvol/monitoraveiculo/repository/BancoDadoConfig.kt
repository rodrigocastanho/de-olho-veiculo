package com.appdesenvol.monitoraveiculo.repository

import android.arch.persistence.room.*
import android.content.Context
import com.appdesenvol.monitoraveiculo.configuration.ConversorData
import com.appdesenvol.monitoraveiculo.model.Veiculo
import com.appdesenvol.monitoraveiculo.model.Manutencao

@Database(entities = [Veiculo::class, Manutencao::class], version = 1, exportSchema = false)
@TypeConverters(ConversorData::class)
abstract class BancoDadoConfig : RoomDatabase() {
    abstract fun controleDAO(): ControleDAO

    companion object{

         fun  getInstance(context: Context):BancoDadoConfig {

            return Room.databaseBuilder(context.applicationContext, BancoDadoConfig::class.java,"Mveiculo").build()

         }

    }

}