package com.appdesenvol.monitoraveiculo.model

import android.arch.persistence.room.*
import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Veiculo")
class Veiculo(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_veiculo") var idV: Long,

    @ColumnInfo(name = "nome_veiculo") var nomeVeiculo: String,
    @ColumnInfo(name = "marca_veiculo") var marcaVeiculo: String,
    @ColumnInfo(name = "placa_veiculo") var placaVeiculo: String,
    @ColumnInfo(name = "motor") var motor: String,
    @ColumnInfo(name = "combustivel") var combustivel: String,
    @ColumnInfo(name = "tipo_cambio") var tipoCambio: String,
    @ColumnInfo(name = "ano") var ano: String
): Parcelable

