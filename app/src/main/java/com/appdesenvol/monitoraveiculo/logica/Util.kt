package com.appdesenvol.monitoraveiculo.logica

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.appdesenvol.monitoraveiculo.bancodados.BancoDadoConfig
import com.appdesenvol.monitoraveiculo.model.Manutencao
import com.appdesenvol.monitoraveiculo.telaStatusManutencao
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import java.sql.SQLException
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


class Util {

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

                    if (s.toString().isNotEmpty()) {

                        valor.removeTextChangedListener(this)
                        val str = s.toString().replace(("[R$,.]").toRegex(), "").trim()
                        val parsed = java.lang.Double.valueOf(str)
                        val formatted = NumberFormat.getCurrencyInstance().format((parsed / 100))
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

    }

}





