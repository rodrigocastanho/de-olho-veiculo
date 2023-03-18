package br.com.devnattiva.deolhoveiculo.configuration

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


class Util {

    companion object STATIC {

        var veiculoId: Long = 0

        fun mascMonetario(valor: Any): TextWatcher {

            lateinit var textWatcherMonetario: TextWatcher
            var valorAtual: String?

            when(valor) {
                   is EditText -> {
                     textWatcherMonetario = object : TextWatcher {

                       override fun afterTextChanged(s: Editable?) {}
                       override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                       override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                           if (s.toString().isNotEmpty()) {

                               valor.removeTextChangedListener(this)
                               val str = s.toString().replace(("[R$,.]").toRegex(), "").trim()
                               val parsed = java.lang.Double.valueOf(str)
                               val formatted =  NumberFormat.getCurrencyInstance().format((parsed / 100))
                               valorAtual = formatted.replace(("[R$]").toRegex(), "")

                               valor.setText(valorAtual)
                               valor.setSelection(valorAtual!!.length)

                               valor.addTextChangedListener(this)
                           }
                       }
                   }
                 }
                 is TextView -> {
                    textWatcherMonetario = object : TextWatcher {

                        override fun afterTextChanged(s: Editable?) {}
                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                            if (s.toString().isNotEmpty()) {

                                valor.removeTextChangedListener(this)
                                val str = s.toString().replace(("[R$]").toRegex(), "")
                                                      .replace(("[,]").toRegex(), ".").trim()
                                val parsed = java.lang.Double.valueOf(str)
                                val formatted = NumberFormat.getCurrencyInstance().format((parsed))
                                valorAtual = formatted

                                valor.text = valorAtual

                                valor.addTextChangedListener(this)
                            }
                        }

                    }
                 }
            }
            return textWatcherMonetario
        }

        fun mascMonetarioTotal(valor: String): String? {
            if(valor.isNotEmpty()) {
                val parsed = valor.toDouble()
                return NumberFormat.getCurrencyInstance().format((parsed))
            }else {
                return ""
            }

        }

        fun pegarVeiculoId(veiculoID: Long) {
            veiculoId = veiculoID
        }

        fun converteDataTexto(data: Date?): String? {
            val dateFormate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            return data?.let { dateFormate.format(it) }
        }

        fun converteTextoData(data: String?): Date? {
            return if(!data.isNullOrEmpty()) {
                val dateFormate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                dateFormate.parse(data)
            } else null

        }

        fun fecharTeclado(context: Activity) {
            val view = context.currentFocus
            view?.let{
                val input = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                input.hideSoftInputFromWindow(it.windowToken, 0)

            }
        }

        fun conversorMonetario(valor: String): String = valor.replace(",",".").trim()

        fun acessoInicial(context: Activity, status: Boolean) {
            context.getPreferences(Context.MODE_PRIVATE)
                .edit()
                .putBoolean("ACESSO_KEY", status)
                .apply()
        }

        fun verificarAcessoInicial(context: Activity): Boolean {
           return context.getPreferences(Context.MODE_PRIVATE).getBoolean("ACESSO_KEY", false)
        }
    }
}





