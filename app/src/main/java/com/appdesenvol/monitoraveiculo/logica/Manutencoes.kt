package com.appdesenvol.monitoraveiculo.logica

import com.appdesenvol.monitoraveiculo.model.Manutencao

interface Manutencoes {

    fun manutencoesConfig(manutencao: Manutencao): ArrayList<Manutencao>

    fun addManutencao(): Manutencao {
       return Manutencao(0, 0, "Nova Manutenção", "", null, "","")
    }

}

