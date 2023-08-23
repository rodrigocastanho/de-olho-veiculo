package br.com.devnattiva.deolhoveiculo.controller

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AlertDialog
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import br.com.devnattiva.deolhoveiculo.repository.BancoDadoConfig
import br.com.devnattiva.deolhoveiculo.model.Veiculo
import br.com.devnattiva.deolhoveiculo.TelaStatusManutencao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.lang.Exception
import java.sql.SQLException


class ControleVeiculo {

    private lateinit var bd: BancoDadoConfig

    fun buscarVeiculo(context: Activity): Pair<MutableList<String>, MutableList<Long>> {
        bd = BancoDadoConfig.getInstance(context.applicationContext)

        val veiculos: Pair<MutableList<String>, MutableList<Long>> =
            Pair(mutableListOf("\t\t\t\t\t\t Buscar Veículo"),
                mutableListOf(0))

        CoroutineScope(IO).launch {
            try {
                val buscveiculo = bd.controleDAO().buscaVeiculo()

                if(buscveiculo.isNotEmpty()) {
                    val buscVeiculos = buscveiculo.sortedBy { it.nomeVeiculo }.toList()

                    for (v: Veiculo in buscVeiculos) {
                        veiculos.first.add(" \t\t\t\t\t\t " + v.nomeVeiculo)
                        veiculos.second.add(v.idV)
                    }
                }
            } catch (e: Exception) {
                Log.e("ERRO_BUSCA_VM", " ERRO_BUSCA_VEICULO_MANUTENÇÂO $e")

            } finally {
                bd.close()
            }
        }
        return veiculos
    }

    fun salvarVeiculo(veiculo: Veiculo, context: Activity) {

        if(veiculo.nomeVeiculo.isNotEmpty()) {
            bd = BancoDadoConfig.getInstance(context.applicationContext)

            CoroutineScope(IO).launch {
                try {
                    when (veiculo.idV) {
                        0L -> bd.controleDAO().salvarDadosVeiculo(veiculo)
                        else ->  bd.controleDAO().alterarDadosVeiculo(veiculo)

                    }
                } catch (e: Exception) {
                    Log.e("ERRO_VEICULO", " SALVAR_VEICULO: $e")
                } finally {
                    bd.close()
                }

            }
            Toast.makeText(context, "VEÍCULO CADASTRADO", Toast.LENGTH_SHORT).show()
            context.startActivity(Intent(context, TelaStatusManutencao::class.java))

        } else {
            Toast.makeText(context, "FALTOU NOME DO VEÍCULO", Toast.LENGTH_SHORT).show()
        }
    }

    //Valor do objeto veiculo vem da class SobreVeiculoDialog
    fun veiculoValorEditado(context: Activity): Veiculo? {
        var veiculoEditado: Veiculo? = null
        try {
            veiculoEditado = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.intent?.getParcelableExtra("veiculoEditado", Veiculo::class.java)
            } else @Suppress("DEPRECATION") context.intent?.getParcelableExtra("veiculoEditado")

        } catch (e: Exception) {
            Log.e("ERRO_PARCEL_VEICULO", "ERRO_PARCEL_VEICULO: $e")
        }
        return veiculoEditado
    }

    fun statusVeiculoId(veiculo: Veiculo?): Long {
        if (veiculo == null) {
            return 0L
        }
        return veiculo.idV
    }


    fun excluirVeiculo(veiculoId: Long, context: Activity) {

        bd = BancoDadoConfig.getInstance(context.applicationContext)

        if (veiculoId != 0L) {

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Excluir veículo")
            builder.setMessage("Deseja excluir o veículo e suas manutenções gravadas?")
            builder.setPositiveButton("SIM") { _, _ ->

                CoroutineScope(IO).launch {
                    try {
                        bd.controleDAO().deletarDadosVeiculo(veiculoId)

                    } catch (e: SQLException) {
                        Log.e("ERRO_DEL_VEICULO", "ERRO_DELETAR_VEICULO_MANUTEÇÂO: $e")

                    } finally {
                        bd.close()
                    }
                }

                context.finish()
                context.overridePendingTransition(0, 0)
                context.startActivity(Intent(context, TelaStatusManutencao::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                Toast.makeText(context.applicationContext, "VEÍCULO EXCLUIDO", Toast.LENGTH_SHORT).show()

            }

            builder.setNegativeButton("NÃO") { _, _ ->
                Toast.makeText(context.applicationContext, "VEÍCULO NÃO SERÁ EXCLUIDO", Toast.LENGTH_SHORT).show()

            }

            builder.setNeutralButton("CANCELAR") { _, _ -> }
            val dialog: AlertDialog = builder.create()
            dialog.show()

        } else {
            Toast.makeText(context.applicationContext, "SELECIONAR VEÍCULO", Toast.LENGTH_SHORT).show()
        }

    }

}