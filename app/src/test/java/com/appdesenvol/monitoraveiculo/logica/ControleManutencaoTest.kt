package com.appdesenvol.monitoraveiculo.logica

import com.appdesenvol.monitoraveiculo.model.Manutencao
import org.junit.Test

class ControleManutencaoTest {

    val arrayManutencoes = ArrayList<Manutencao>()


    init {


        arrayManutencoes.add(Manutencao(0, 0, "Oleo de Motor", "", "", ""))
        arrayManutencoes.add(Manutencao(0, 0, "Oleo de Cambio", "", "", ""))
        arrayManutencoes.add(Manutencao(0, 0, "Filtro de Ar", "", "", ""))
        arrayManutencoes.add(Manutencao(0, 0, "Filtro de Combustivel", "", "", ""))
        arrayManutencoes.add(Manutencao(0, 0, "Fluido de freio", "", "", ""))


    }


    @Test
    fun resultManutencao(tipoManutencao: String, kmtroca: String, data: String, custo: String) {


        for (indx in arrayManutencoes.indices) {


            if (tipoManutencao == arrayManutencoes[indx].tipoManutencao) {

                arrayManutencoes.set(indx,
                    Manutencao(0, 0, arrayManutencoes[indx].tipoManutencao, kmtroca, data, custo)
                )

                //Log.i("INDX ", " INDX " + indx)
                //Log.i("KMTROCA ", " BD " + tipoManutencao)
                //Log.i("KMTROCA ", " ARRAY " + arrayManutencoes[indx].tipoManutencao)
                //Log.i("KMTROCA ", " BD " + kmtroca)

            }

        }

    }

}