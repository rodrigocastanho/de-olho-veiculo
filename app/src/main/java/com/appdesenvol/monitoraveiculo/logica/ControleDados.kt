package com.appdesenvol.monitoraveiculo.logica

import android.app.Activity
import android.arch.persistence.room.Room
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION
import android.content.IntentFilter
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.appdesenvol.monitoraveiculo.bancodados.BancoDadoConfig
import com.appdesenvol.monitoraveiculo.objetos.Manutencao
import com.appdesenvol.monitoraveiculo.telaStatusManutencao
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import java.sql.SQLException
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList


class ControleDados {

    companion object STATIC {

        private fun replaceChars(textoEditText: String): String {
            return textoEditText.replace(".", "").replace("-", "")
                .replace("(", "").replace(")", "")
                .replace("/", "").replace(" ", "")
                .replace("*", "")
        }

        fun mascEdittext(masc: String, texto: EditText): TextWatcher {

            val textWatcher: TextWatcher = object : TextWatcher {

                var isUpdating: Boolean = false
                var oldString: String = ""

                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    val str = replaceChars(s.toString())
                    var escritamasc = ""

                    if (count == 0)//is deleting
                        isUpdating = true

                    if (isUpdating) {
                        oldString = str
                        isUpdating = false
                        return
                    }

                    var i = 0
                    for (m: Char in masc.toCharArray()) {

                        if (m != '#' && str.length > oldString.length) {

                            escritamasc += m
                            continue
                        }
                        try {
                            escritamasc += str.get(i)


                        } catch (e: Exception) {
                            break
                        }
                        i++
                    }

                    isUpdating = true
                    texto.setText(escritamasc)
                    texto.setSelection(escritamasc.length)
                }
            }
            return textWatcher
        }

        fun mascMonetario(valor: EditText): TextWatcher {

            val textWatcherMonetario: TextWatcher = object : TextWatcher {

                var valorAtual = ""

                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    if (s.toString() != valorAtual) {

                        valor.removeTextChangedListener(this)
                        val str = s.toString().replace(("[R$,.]").toRegex(), "")
                        val parsed = java.lang.Double.valueOf(str)
                        val formatted = DecimalFormat.getCurrencyInstance().format((parsed / 100))
                        valorAtual = formatted.replace(("[R$]").toRegex(), "")

                        valor.setText(valorAtual)
                        valor.setSelection(valorAtual.length)

                        valor.addTextChangedListener(this)
                    }
                }
            }
            return textWatcherMonetario
        }

        var veiculoId: Long = 0

        fun pegarVeiculoId(veiculoID: Long) {

            veiculoId = veiculoID

        }

        fun salvarManutencao(manutencao: Manutencao, context: Context) {

            val bd = BancoDadoConfig.getInstance(context.applicationContext)


            if(manutencao.kmtroca.isNotEmpty() || manutencao.data.isNotEmpty() || manutencao.custo.isNotEmpty()) {

                CoroutineScope(IO).launch {

                    try {

                        bd.controleDAO().salvarDadosManutencao(manutencao)

                    } catch (e: SQLException) {

                        Log.e("ERRO Manutencao insert", " VERIFICAR ERRO: " + e)

                    }

                }

                Toast.makeText(context.applicationContext, " MANUTENÇÃO GRAVADA " + manutencao.tipoManutencao.toUpperCase(), Toast.LENGTH_SHORT).show()


            }else{

                Toast.makeText(context.applicationContext, "ADICIONE PELO MENOS UMA INFORMAÇÃOS EM UM DOS CAMPOS: " + manutencao.tipoManutencao.toUpperCase(), Toast.LENGTH_LONG).show()

            }


        }

        fun excluirVeiculo(veiculoId: Long, context: Activity){

            val bd = BancoDadoConfig.getInstance(context.applicationContext)

            if (veiculoId != 0L) {

                val builder = AlertDialog.Builder(context)

                builder.setTitle("Excluir veiculo")

                builder.setMessage("Deseja excluir o veiculo e suas manutenções gravadas?")


                builder.setPositiveButton("SIM") { _, _ ->


                    CoroutineScope(IO).launch {

                        try {

                            bd.controleDAO().deletarDadosVeiculo(veiculoId)

                        } catch (e: SQLException) {

                            Log.e("ERRO Manutencao delete", " VERIFICAR ERRO: " + e)


                        }
                    }

                    context.finish()
                    context.overridePendingTransition(0, 0)
                    context.startActivity(
                        Intent(context, telaStatusManutencao::class.java).addFlags(
                            FLAG_ACTIVITY_NO_ANIMATION
                        )
                    )

                    Toast.makeText(context.getApplicationContext(), "VEICULO EXCLUIDO", Toast.LENGTH_SHORT).show()

                }

                builder.setNegativeButton("NÃO") { _, _ ->

                    Toast.makeText(context.getApplicationContext(), "VEICULO NÃO SERÁ EXCLUIDO", Toast.LENGTH_SHORT).show()

                }

                builder.setNeutralButton("CANCELAR") { _, _ -> }

                val dialog: AlertDialog = builder.create()

                dialog.show()


            } else {

                Toast.makeText(context.getApplicationContext(), "SELECIONAR VEICULO", Toast.LENGTH_SHORT).show()

            }


        }


    }

}





