package com.appdesenvol.monitoraveiculo.objetos

import android.arch.persistence.room.*
import com.appdesenvol.monitoraveiculo.logica.ControleDados
import com.appdesenvol.monitoraveiculo.logica.ConversorListManutencao

@Entity(tableName = "Veiculo")
class Veiculo(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_veiculo") val idV: Long,

    @ColumnInfo(name = "nome_veiculo") var nomeVeiculo: String,
    @ColumnInfo(name = "marca_veiculo") var marcaVeiculo: String,
    @ColumnInfo(name = "placa_veiculo") var placaVeiculo: String,
    @ColumnInfo(name = "motor") var motor: String,
    @ColumnInfo(name = "combustivel") var combustivel: String,
    @ColumnInfo(name = "tipo_cambio") var tipoCambio: String,
    @ColumnInfo(name = "ano") var ano: String
)

