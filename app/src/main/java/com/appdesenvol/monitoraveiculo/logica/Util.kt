package com.appdesenvol.monitoraveiculo.logica

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.persistence.room.TypeConverter
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat.getSystemService
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import kotlinx.android.synthetic.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*


class Util {

    companion object STATIC {

        var veiculoId: Long = 0

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

        fun pegarVeiculoId(veiculoID: Long) {
            veiculoId = veiculoID
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun converteDataTexto(data: LocalDate?): String? {
            return data?.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun converteTextoData(data: String?): LocalDate? {
            if(!data.isNullOrEmpty()) {
                return LocalDate.parse(data, DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
            }else return null

        }

        fun fecharTeclado(context: Activity) {
            val view = context.currentFocus
            view?.let{
                val input = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                input.hideSoftInputFromWindow(it.windowToken, 0)

            }

        }

        fun conversorMonetario(valor: String): String = valor.replace(",",".").trim()


    }

}





