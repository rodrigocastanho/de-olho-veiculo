package br.com.devnattiva.deolhoveiculo.controller

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import br.com.devnattiva.deolhoveiculo.AvisoDialog
import br.com.devnattiva.deolhoveiculo.TelaStatusManutencao
import br.com.devnattiva.deolhoveiculo.model.Veiculo
import br.com.devnattiva.deolhoveiculo.repository.BancoDadoConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    fun salvarVeiculo(
        veiculo: Veiculo,
        context: Activity,
        callBack: (messagem: String) -> Unit
    ) {

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
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "VEÍCULO CADASTRADO", Toast.LENGTH_SHORT).show()
                    context.startActivity(Intent(context, TelaStatusManutencao::class.java))
                }
            }
        } else {
            callBack.invoke("Obrigatório nome do veículo")
        }
    }

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
            AvisoDialog()
                .createDialog(
                    context = context,
                    title = "Excluir veículo",
                    messageText = "Deseja excluir o veículo e suas manutenções gravadas?",
                    primaryButtonAction = { dialog ->
                        CoroutineScope(IO).launch {
                            try {
                                bd.controleDAO().deletarDadosVeiculo(veiculoId)
                                withContext(Dispatchers.Main) {
                                    dialog.dismiss()
                                    context.startActivity(Intent(context, TelaStatusManutencao::class.java)
                                        .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                                    Toast.makeText(context.applicationContext, "VEÍCULO EXCLUIDO",Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: SQLException) {
                                dialog.dismiss()
                                Log.e("ERRO_DEL_VEICULO", "ERRO_DELETAR_VEICULO_MANUTEÇÂO: $e")
                            } finally {
                                bd.close()
                            }
                        }
                    },
                    secundaryButtonAction = { dialog ->
                        dialog.dismiss()
                        Toast.makeText(context.applicationContext, "VEÍCULO NÃO SERÁ EXCLUIDO",Toast.LENGTH_SHORT).show()
                    },
                    thirdButtonAction = {dialog ->  dialog.dismiss() }
                )
        } else {
            Toast.makeText(context.applicationContext, "SELECIONAR VEÍCULO", Toast.LENGTH_SHORT).show()
        }
    }
}