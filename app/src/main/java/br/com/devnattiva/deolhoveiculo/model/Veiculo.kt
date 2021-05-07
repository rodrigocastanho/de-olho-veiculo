package br.com.devnattiva.deolhoveiculo.model

import android.arch.persistence.room.*
import android.os.Parcel
import android.os.Parcelable
import com.opencsv.bean.CsvBindByName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Veiculo")
class Veiculo(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_veiculo")    @CsvBindByName var idV: Long,

    @ColumnInfo(name = "nome_veiculo")  @CsvBindByName var nomeVeiculo: String,
    @ColumnInfo(name = "marca_veiculo") @CsvBindByName var marcaVeiculo: String,
    @ColumnInfo(name = "placa_veiculo") @CsvBindByName var placaVeiculo: String,
    @ColumnInfo(name = "motor")         @CsvBindByName var motor: String,
    @ColumnInfo(name = "combustivel")   @CsvBindByName var combustivel: String,
    @ColumnInfo(name = "tipo_cambio")   @CsvBindByName var tipoCambio: String,
    @ColumnInfo(name = "ano")           @CsvBindByName var ano: String
): Parcelable {

    @Ignore
    constructor() : this(0L,"","","","","","","")
}


