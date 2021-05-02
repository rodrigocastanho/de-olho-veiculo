package com.appdesenvol.monitoraveiculo.repository

import android.arch.persistence.room.*
import com.appdesenvol.monitoraveiculo.model.VeiculoManutencao
import com.appdesenvol.monitoraveiculo.model.Manutencao
import com.appdesenvol.monitoraveiculo.model.Veiculo

@Dao
interface ControleDAO {

    //SELECT utilizado no campo Buscar Veiculo da tela "Status Manutençao"
    //Fazer que também as informação apareça nos campos quando clicar no Botão Sobre Veiculo
    @Query("SELECT * FROM Veiculo")
    fun buscaVeiculo(): List<Veiculo>

    @Query("SELECT * FROM Veiculo WHERE Veiculo.id_veiculo = :idVeiculo")
    fun buscaVeiculoId(idVeiculo: Long): Veiculo

    @Insert
    fun salvarDadosVeiculo(veiculo: Veiculo)

    @Update
    fun alterarDadosVeiculo(veiculo: Veiculo)

    @Query("DELETE FROM Veiculo WHERE id_veiculo = :idVeiculo")
    fun deletarDadosVeiculo(idVeiculo: Long)

    @Delete
    fun deletarDadosVeiculo(veiculo: Veiculo)


/*-------------------------------------Manutencão---------------------------------------*/

    @Query("SELECT * FROM Veiculo INNER JOIN Manutencao ON Veiculo.id_veiculo = Manutencao.id_mveiculo WHERE Manutencao.id_mveiculo = :idVeiculo")
    fun buscaVeiculoManutencao(idVeiculo: Long): List<VeiculoManutencao>

    @Insert
    fun salvarDadosManutencao(manutencao: Manutencao)

    @Update
    fun alterarDadosManutencao(manutencao: Manutencao)

    @Delete
    fun deletarDadosManutencao(manutencao: Manutencao)

/*-------------------------------------Backup/Restaura---------------------------------------*/
   @Transaction
   fun salvarDadosVeiculoBKP(veiculo: Veiculo) {
        deletarDadosVeiculo(veiculo)
        salvarDadosVeiculo(veiculo)
    }

    @Insert
    fun salvarDadosManutencaoBKP(manutencoes: List<Manutencao>)

    @Query("SELECT * FROM Veiculo INNER JOIN Manutencao ON Veiculo.id_veiculo = Manutencao.id_mveiculo")
    fun buscaDadosBKP(): List<VeiculoManutencao>



}