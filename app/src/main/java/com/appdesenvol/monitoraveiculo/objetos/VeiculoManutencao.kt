package com.appdesenvol.monitoraveiculo.objetos

import android.arch.persistence.room.Embedded

class VeiculoManutencao(

    @Embedded var veiculo: Veiculo,

    @Embedded var manutencao: Manutencao
)