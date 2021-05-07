package br.com.devnattiva.deolhoveiculo.controller

import br.com.devnattiva.deolhoveiculo.model.Manutencao

interface Manutencoes {

    fun manutencoesConfig(manutencao: Manutencao): ArrayList<Manutencao>

    fun addManutencao(): Manutencao {
       return Manutencao(0, 0, "Nova Manutenção", "", null, "","")
    }

}

