package com.appdesenvol.monitoraveiculo.controller

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.Toast
import com.appdesenvol.monitoraveiculo.repository.BancoDadoConfig
import com.appdesenvol.monitoraveiculo.model.Veiculo
import com.appdesenvol.monitoraveiculo.TelaStatusManutencao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.lang.Exception
import java.sql.SQLException


class ControleVeiculo {

    private lateinit var bd: BancoDadoConfig

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
                    Log.e("ERRO_VEICULO", " SALVAR_VEICULO: " + e)
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
            veiculoEditado = context.intent?.getParcelableExtra<Veiculo>("veiculoEditado")
        } catch (e: Exception) {
            Log.e("ERRO_PARCEL_VEICULO", "ERRO_PARCEL_VEICULO: " + e)
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

                CoroutineScope(Dispatchers.IO).launch {

                    try {

                        bd.controleDAO().deletarDadosVeiculo(veiculoId)

                    } catch (e: SQLException) {
                        Log.e("ERRO_DEL_VEICULO", "ERRO_DELETAR_VEICULO_MANUTEÇÂO: " + e)

                    }
                }

                context.finish()
                context.overridePendingTransition(0, 0)
                context.startActivity(
                    Intent(context, TelaStatusManutencao::class.java).addFlags(
                        Intent.FLAG_ACTIVITY_NO_ANIMATION
                    )
                )
                Toast.makeText(context.getApplicationContext(), "VEÍCULO EXCLUIDO", Toast.LENGTH_SHORT).show()

            }

            builder.setNegativeButton("NÃO") { _, _ ->
                Toast.makeText(context.getApplicationContext(), "VEÍCULO NÃO SERÁ EXCLUIDO", Toast.LENGTH_SHORT).show()

            }

            builder.setNeutralButton("CANCELAR") { _, _ -> }
            val dialog: AlertDialog = builder.create()
            dialog.show()

        } else {
            Toast.makeText(context.getApplicationContext(), "SELECIONAR VEÍCULO", Toast.LENGTH_SHORT).show()
        }

    }

//    @SuppressLint("CommitPrefEdits")
//    fun controleSalvarVeiculoVersao(veiculoSalvo: Int, context: Activity) {
//      val sharePref = context.getPreferences(Context.MODE_PRIVATE)?: return
//        with(sharePref.edit()) {
//           putInt("VS_GRATIS", veiculoSalvo)
//            commit()
//        }
//    }

}