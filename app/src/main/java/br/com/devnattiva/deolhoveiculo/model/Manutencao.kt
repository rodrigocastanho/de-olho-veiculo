package br.com.devnattiva.deolhoveiculo.model

import androidx.room.*
import androidx.room.ForeignKey.Companion.CASCADE
import com.opencsv.bean.CsvBindByName
import com.opencsv.bean.CsvDate
import java.util.*

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
    @ColumnInfo(name = "data")            @CsvDate("yyyy-MM-dd") var data: Date?,
    @ColumnInfo(name = "custo")           @CsvBindByName var custo: String,
    @ColumnInfo(name = "observacao")      @CsvBindByName var observacao: String) {
    @Ignore
    constructor(): this(0L,0L,"","", "",null,"","")

    fun validacao(): Pair<Map<Int, String>, Boolean> {
        val campos = mutableMapOf<Int, String>()

        if (this.kmtrocaAtual.isBlank()) {
            campos[0] = "Obrigatório"
        } else campos[0] = ""

        if (this.kmtroca.isBlank()) {
            campos[1] = "Obrigatório"
        } else campos[1] = ""

        if (this.data == null) {
            campos[2] = "Oobrigatório"
        } else campos[2] = ""

        if (this.custo.isBlank()) {
            campos[3] = "Obrigatório"
        } else campos[3] = ""

        if (this.tipoManutencao.isBlank()) {
            campos[4] = "Obrigatório"
        } else campos[4] = ""
        val erro = campos.values.any { erro -> erro != "" }

        return Pair(campos, !erro)
    }
}





