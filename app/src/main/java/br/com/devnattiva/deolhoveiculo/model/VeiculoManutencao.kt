package br.com.devnattiva.deolhoveiculo.model

import androidx.room.Embedded
import androidx.room.Ignore
import com.opencsv.bean.CsvRecurse

class VeiculoManutencao(

    @Embedded @CsvRecurse var veiculo: Veiculo,

    @Embedded @CsvRecurse var manutencao: Manutencao
) {
    @Ignore
    constructor() : this(Veiculo(), Manutencao())
}