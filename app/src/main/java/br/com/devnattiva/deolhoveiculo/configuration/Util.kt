package br.com.devnattiva.deolhoveiculo.configuration

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.text.isDigitsOnly
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputLayout
import java.lang.IndexOutOfBoundsException
import java.lang.StringBuilder
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


class Util {

    companion object STATIC {
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
                               val str = s.toString().replace(("[R$,.]").toRegex(),"").trim()
                               val parsed = java.lang.Double.valueOf(str)
                               val formatted =  NumberFormat.getCurrencyInstance().format((parsed / 100))
                               valorAtual = formatted.replace(("[R$]").toRegex(),"").trim()

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
                                val str = s.toString().replace(("[R$,.]").toRegex(),"").trim()
                                val parsed = java.lang.Double.valueOf(str)
                                val formatted = NumberFormat.getCurrencyInstance().format((parsed / 100))
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
            return if(valor.isNotEmpty()) {
                val parsed = valor.toDouble()
                NumberFormat.getCurrencyInstance().format((parsed))
            } else ""
        }

        //100
        //1.000 = 3
        //10.000 = 4
        //100.000 = 5
        fun mascKmValor(edit: EditText):TextWatcher {
            return object : TextWatcher {
                override fun afterTextChanged(conteudo: Editable?) {}
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    edit.removeTextChangedListener(this)
                    val valor = StringBuilder().append(s)
                    if (before != 1) {
                        if (!valor.contains(".")) {
                            if (start in 3..6) {
                                valor.insert(1, ".")
                            }
                        } else {
                            if (start in 4..5) {
                                valor.deleteCharAt(1)
                                valor.insert(1, "")
                                valor.insert(2, ".")
                            } else {
                                if (start in 5..6) {
                                    valor.deleteCharAt(2)
                                    valor.insert(2, "")
                                    valor.insert(3, ".")
                                }
                            }
                        }
                        if (s.toString().isNotEmpty()) {
                            if (!valor.contains(".")) {
                                when ((count - 1)) {
                                    3 -> valor.insert(1, ".")
                                    4 -> valor.insert(2, ".")
                                    5 -> valor.insert(3, ".")
                                }
                            }
                        }
                    }

                    edit.setText(valor.toString())
                    edit.setSelection(valor.length)
                    edit.addTextChangedListener(this)
                }
            }
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

        fun acessoInicial(context: Activity, status: Boolean) {
            context.getPreferences(Context.MODE_PRIVATE)
                .edit()
                .putBoolean("ACESSO_KEY", status)
                .apply()
        }

        fun verificarAcessoInicial(context: Activity): Boolean {
           return context.getPreferences(Context.MODE_PRIVATE).getBoolean("ACESSO_KEY", false)
        }

        fun limparErroCampo(editText: EditText, textInput: TextInputLayout) {
            editText.doAfterTextChanged {
                textInput.isErrorEnabled = false
                textInput.error = null
            }
        }
    }
}





