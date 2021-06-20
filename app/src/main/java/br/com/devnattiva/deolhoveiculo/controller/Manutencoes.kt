package br.com.devnattiva.deolhoveiculo.controller

import br.com.devnattiva.deolhoveiculo.model.Manutencao
import java.util.*

interface Manutencoes {

    fun manutencoesConfig(manutencao: Manutencao): LinkedList<Manutencao>

    fun addManutencao(): Manutencao {
       return Manutencao(0, 0, "Nova Manutenção", "", null, "","")
    }

}

