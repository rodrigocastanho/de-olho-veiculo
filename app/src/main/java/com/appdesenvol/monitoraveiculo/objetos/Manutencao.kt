package com.appdesenvol.monitoraveiculo.objetos

import android.arch.persistence.room.*
import android.arch.persistence.room.ForeignKey.CASCADE

@Entity(
    tableName = "Manutencao",
    indices = arrayOf(
        Index(value = ["id_mveiculo", "tipo_manutencao", "kmtroca", "data", "custo"])
    ),
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
    @ColumnInfo(name = "id_manutencao") val idM: Long,

    @ColumnInfo(name = "id_mveiculo") val idVM: Long,
    @ColumnInfo(name = "tipo_manutencao") var tipoManutencao: String,
    @ColumnInfo(name = "kmtroca") var kmtroca: String,
    @ColumnInfo(name = "data") var data: String,
    @ColumnInfo(name = "custo") var custo: String


)




