package com.appdesenvol.monitoraveiculo.model

import android.arch.persistence.room.Embedded

class VeiculoManutencao(

    @Embedded var veiculo: Veiculo,

    @Embedded var manutencao: Manutencao
)