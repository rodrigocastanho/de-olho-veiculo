package com.appdesenvol.monitoraveiculo.logica

import android.arch.persistence.room.TypeConverter
import com.appdesenvol.monitoraveiculo.objetos.Manutencao
import com.google.gson.Gson

class ConversorListManutencao {

    @TypeConverter
    fun listparaJson(manutencao: List<Manutencao>?): String {

        return Gson().toJson(manutencao)
    }
    @TypeConverter
    fun jsonparaList(manutencao: String): List<Manutencao>? {

        val gsonlist = Gson().fromJson(manutencao, Array<Manutencao>::class.java) as Array<Manutencao>
        val manutencaolist = gsonlist.toList()

        return manutencaolist

    }

}
