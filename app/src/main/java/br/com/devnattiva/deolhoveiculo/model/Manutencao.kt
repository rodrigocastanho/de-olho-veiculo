package br.com.devnattiva.deolhoveiculo.model

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.opencsv.bean.CsvBindByName
import com.opencsv.bean.CsvDate
import java.time.LocalDate

@Entity(
    tableName = "Manutencao",
    indices = [Index(value = ["id_mveiculo", "tipo_manutencao", "kmtroca", "data", "custo"])],
    foreignKeys = [
        ForeignKey(
            entity = Veiculo::class,
            parentColumns = ["id_veiculo"],
            childColumns = ["id_mveiculo"],
            onDelete = CASCADE
        )
    ]
)
class Manutencao(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_manutencao")   @CsvBindByName var idM: Long,
    @ColumnInfo(name = "id_mveiculo")     @CsvBindByName var idVM: Long,
    @ColumnInfo(name = "tipo_manutencao") @CsvBindByName var tipoManutencao: String,
    @ColumnInfo(name = "kmtroca_atual")   @CsvBindByName var kmtrocaAtual: String,

    @ColumnInfo(name = "kmtroca")         @CsvBindByName var kmtroca: String,
    @ColumnInfo(name = "data")            @CsvDate("yyyy-MM-dd") var data: LocalDate?,
    @ColumnInfo(name = "custo")           @CsvBindByName var custo: String,
    @ColumnInfo(name = "observacao")      @CsvBindByName var observacao: String) {

    @Ignore
    constructor(): this(0L,0L,"","", "",null,"","")

}




