package com.appdesenvol.monitoraveiculo.bancodados

import android.arch.persistence.room.*
import android.content.Context
import com.appdesenvol.monitoraveiculo.model.Veiculo
import com.appdesenvol.monitoraveiculo.model.Manutencao

@Database(entities = [Veiculo::class, Manutencao::class], version = 1)
//@TypeConverters(ConversorListManutencao::class)
abstract class BancoDadoConfig : RoomDatabase() {
    abstract fun controleDAO(): ControleDAO

    companion object{

         fun  getInstance(context: Context):BancoDadoConfig {

            return Room.databaseBuilder(context.applicationContext, BancoDadoConfig::class.java,"Mveiculo").build()

         }

    }

}