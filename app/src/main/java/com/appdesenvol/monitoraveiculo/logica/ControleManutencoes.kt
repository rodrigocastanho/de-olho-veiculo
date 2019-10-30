package com.appdesenvol.monitoraveiculo.logica

import android.content.Context
import android.database.SQLException
import android.util.Log
import com.appdesenvol.monitoraveiculo.bancodados.BancoDadoConfig
import com.appdesenvol.monitoraveiculo.objetos.Manutencao
import kotlinx.android.synthetic.main.content_tela_status_manutencao.*
import java.util.*
import kotlin.collections.ArrayList


class ControleManutencoes {

    val arrayManutencoes = ArrayList<Manutencao>()


    init {

        arrayManutencoes.add(Manutencao(0, 0, "Oleo de Motor", "", "", ""))
        arrayManutencoes.add(Manutencao(0, 0, "Oleo de Cambio", "", "", ""))
        arrayManutencoes.add(Manutencao(0, 0, "Filtro de Ar", "", "", ""))
        arrayManutencoes.add(Manutencao(0, 0, "Filtro de Combustivel", "", "", ""))
        arrayManutencoes.add(Manutencao(0, 0, "Fluido de freio", "", "", ""))


    }

    fun resultManutencao(tipoManutencao: String, kmtroca: String, data: String, custo: String) {


        for (indx in 0 until arrayManutencoes.size) {

            if(tipoManutencao == arrayManutencoes[indx].tipoManutencao) {

                arrayManutencoes.set(indx, Manutencao(0, 0, arrayManutencoes[indx].tipoManutencao, kmtroca, data, custo))

                Log.i("INDX ", " INDX " + indx)
                Log.i("KMTROCA ", " BD " + tipoManutencao)
                Log.i("KMTROCA ", " ARRAY " + arrayManutencoes[indx].tipoManutencao)
                Log.i("KMTROCA ", " BD " + kmtroca)



            }

        }


    }


}