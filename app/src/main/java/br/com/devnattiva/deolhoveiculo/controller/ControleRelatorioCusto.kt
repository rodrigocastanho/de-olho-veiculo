package br.com.devnattiva.deolhoveiculo.controller

import android.app.Activity
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import br.com.devnattiva.deolhoveiculo.DatePickerFragmentDialog
import br.com.devnattiva.deolhoveiculo.configuration.Util
import br.com.devnattiva.deolhoveiculo.databinding.ActivityTelaRelatorioCustoBinding
import br.com.devnattiva.deolhoveiculo.model.Manutencao
import br.com.devnattiva.deolhoveiculo.repository.BancoDadoConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class ControleRelatorioCusto {

    private lateinit var bd: BancoDadoConfig


    fun buscaVeiculoCusto(context: Activity): Map<Long, String> {
        bd = BancoDadoConfig.getInstance(context.applicationContext)
        val veiculos = mutableMapOf(0L to "\t\t\t\t\t Buscar Veículo")

        CoroutineScope(IO).launch {
            try {
                bd.controleDAO().buscaVeiculo().forEach { veiculos[it.idV] = "\t\t\t\t\t\t" +it.nomeVeiculo }
            }catch (e: Exception) {
                Log.e("ERRO_BUSCA_V_CUSTO", " ERRO_BUSCA_VEICULO_CUSTO $e")
            }finally {
                bd.close()
            }
        }
        return veiculos

    }

    fun filtrarCustoManutencao(idVeiculo: Long, dataInicial: EditText, dataFinal: EditText, context: Activity, viewRelatorioCustoBind: ActivityTelaRelatorioCustoBinding) {
        bd = BancoDadoConfig.getInstance(context.applicationContext)

        try {
            DatePickerFragmentDialog().verificarIntervaloData(dataInicial, dataFinal)
            val dtInicial = Util.converteTextoData(dataInicial.text.toString())
            var dtFinal = Util.converteTextoData(dataFinal.text.toString())

            if(dtFinal == null) {
                dtFinal = dtInicial
            }

            CoroutineScope(IO).launch {
                val manutencoes = bd.controleDAO().buscarCustoData(idVeiculo, dtInicial, dtFinal)
                withContext(Main) {
                    viewRelatorioCustoBind.rcCusto.adapter = CustoManutencaoAdapterRW(manutencoes, context)
                    viewRelatorioCustoBind.txtTotalCustoValor?.text = Util.mascMonetarioTotal(calcularTotalCusto(manutencoes).toString())
                    
                }
            }

        }catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        } finally {
            bd.close()
        }

    }

    fun alterarEstadoCamposDataBotao(idVeiculo: Long, viewRelatorioCustoBind: ActivityTelaRelatorioCustoBinding) {
        if(idVeiculo == 0L) {
            viewRelatorioCustoBind.edDataInicialCusto.isEnabled = false
            viewRelatorioCustoBind.edDataFinalCusto.isEnabled = false
            viewRelatorioCustoBind.btFiltroCusto?.isEnabled = false
            viewRelatorioCustoBind.rcCusto.adapter = CustoManutencaoAdapterRW(listOf(), viewRelatorioCustoBind.root.context)
            viewRelatorioCustoBind.txtTotalCustoValor?.text = ""
        } else {
            viewRelatorioCustoBind.edDataInicialCusto.isEnabled = true
            viewRelatorioCustoBind.edDataFinalCusto.isEnabled = true
            viewRelatorioCustoBind.btFiltroCusto?.isEnabled = true
            viewRelatorioCustoBind.rcCusto.adapter = CustoManutencaoAdapterRW(listOf(), viewRelatorioCustoBind.root.context)
            viewRelatorioCustoBind.txtTotalCustoValor?.text = ""

        }

    }
    
     private fun calcularTotalCusto(manutencoes: List<Manutencao>): Double {
        var total = 0.0
        manutencoes.forEach { m -> total += (m.custo.toDouble()) }
        return total

    }


}