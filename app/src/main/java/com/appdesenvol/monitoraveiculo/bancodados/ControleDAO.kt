package com.appdesenvol.monitoraveiculo.bancodados

import android.arch.persistence.room.*
import com.appdesenvol.monitoraveiculo.objetos.VeiculoManutencao
import com.appdesenvol.monitoraveiculo.objetos.Manutencao
import com.appdesenvol.monitoraveiculo.objetos.Veiculo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

@Dao
interface ControleDAO {

    //SELECT utilizado no campo Buscar Veiculo da tela "Status Manutençao"
    //Fazer que também as informação apareça no nos campos quando clicar no Botão Sobre Veiculo
    @Query("SELECT * FROM Veiculo")
    fun buscaVeiculo(): List<Veiculo>

    @Query("SELECT * FROM Veiculo INNER JOIN Manutencao ON Veiculo.id_veiculo = Manutencao.id_mveiculo WHERE Manutencao.id_mveiculo = :idVeiculo")
    fun buscaVeiculoManutencao(idVeiculo: Long): List<VeiculoManutencao>

    @Insert
    fun salvarDados(veiculo: Veiculo)
    //@Update
    //fun alterarDados(veiculo: Veiculo)

    @Query("DELETE FROM Veiculo WHERE id_veiculo = :idVeiculo")
    fun deletarDadosVeiculo(idVeiculo: Long)

/*-------------------------------------Entidade Manutencão---------------------------------------*/

    //@Query("SELECT * FROM Manutencao WHERE id_mveiculo = :idVeiculo")
     //fun buscaManutencao(idVeiculo: Long): List<Manutencao>

    @Insert
     fun salvarDadosManutencao(manutencao: Manutencao)


}