package com.appdesenvol.monitoraveiculo.logica

import com.appdesenvol.monitoraveiculo.model.Manutencao

interface Manutencoes {

    fun manutencoesConfig() = arrayListOf<Manutencao>().apply {
        add(Manutencao(0, 0, "Óleo de motor", "", "", "",""))
        add(Manutencao(0, 0, "Óleo de câmbio", "", "", "",""))
        add(Manutencao(0, 0, "Filtro de óleo", "", "", "",""))
        add(Manutencao(0, 0, "Filtro de ar/motor", "", "", "",""))
        add(Manutencao(0, 0, "Filtro de ar/ar-condicionado", "", "", "",""))
        add(Manutencao(0, 0, "Filtro de combustível", "", "", "",""))
        add(Manutencao(0, 0, "Fluido de freio", "", "", "",""))
        add(Manutencao(0, 0, "Correias", "", "", "",""))
        add(Manutencao(0, 0, "Velas de ignição", "", "", "",""))
        add(Manutencao(0, 0, "Câmbio/Embreagem", "", "", "",""))
        add(Manutencao(0, 0, "Pastilha de freio", "", "", "",""))
        add(Manutencao(0, 0, "Disco/Lonas/Tambores de freio", "", "", "",""))
        add(Manutencao(0, 0, "Radiador/Aditivos", "", "", "",""))
        add(Manutencao(0, 0, "Amortecedores/Molas", "", "", "",""))
        add(Manutencao(0, 0, "Pneus", "", "", "",""))

    }

}

