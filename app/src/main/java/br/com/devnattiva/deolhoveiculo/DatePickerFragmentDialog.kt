package br.com.devnattiva.deolhoveiculo

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import br.com.devnattiva.deolhoveiculo.configuration.Util
import java.util.*

class DatePickerFragmentDialog: DialogFragment(), DatePickerDialog.OnDateSetListener {

    private var campoData: EditText?= null
    private val MESSANGEM_INTERVALO_DATA = "Data inicial não pode ser maior que a data final."
    private val calendario = Calendar.getInstance()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val ano = calendario.get(Calendar.YEAR)
        val mes = calendario.get(Calendar.MONTH)
        val dia = calendario.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(activity as Context, this, ano, mes, dia)
    }

    override fun onDateSet(view: DatePicker, ano: Int, mes: Int, dia: Int) {
        calendario.set(ano, mes, dia)
        val data = calendario.time
        campoData?.setText(Util.converteDataTexto(data))
    }

    fun exibirDataPicker(supportFragmentManager: FragmentManager, editText: EditText) {
        campoData = editText
        this.show(supportFragmentManager,"dialogDatePicker")
    }

    @Throws(Exception::class)
    fun verificarIntervaloData(dataInicial: EditText, dataFinal: EditText) {
        val dtInicial = Util.converteTextoData(dataInicial.text.toString())
        val dtFinal = Util.converteTextoData(dataFinal.text.toString())

        if(dtInicial != null && dtFinal != null) {
            if (dtInicial.after(dtFinal)) {
                throw Exception(MESSANGEM_INTERVALO_DATA)
            }
        }

    }
}