package com.appdesenvol.monitoraveiculo.model

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Ignore
import com.opencsv.bean.CsvRecurse

class VeiculoManutencao(

    @Embedded @CsvRecurse var veiculo: Veiculo,

    @Embedded @CsvRecurse var manutencao: Manutencao
) {
    @Ignore
    constructor() : this(Veiculo(), Manutencao())
}