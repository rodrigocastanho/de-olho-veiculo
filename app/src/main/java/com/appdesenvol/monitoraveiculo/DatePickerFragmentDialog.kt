package com.appdesenvol.monitoraveiculo

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.widget.DatePicker
import android.widget.EditText
import com.appdesenvol.monitoraveiculo.configuration.Util
import java.time.LocalDate
import java.util.*

class DatePickerFragmentDialog: DialogFragment(), DatePickerDialog.OnDateSetListener {

    var campoData: EditText?= null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val calendario = Calendar.getInstance()
        val ano = calendario.get(Calendar.YEAR)
        val mes = calendario.get(Calendar.MONTH)
        val dia = calendario.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(activity as Context, this, ano, mes, dia)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDateSet(view: DatePicker, ano: Int, mes: Int, dia: Int) {
        val data = LocalDate.of(ano, mes + 1, dia)
        campoData?.setText(Util.converteDataTexto(data))

    }

    fun exibirDataPicker(supportFragmentManager: FragmentManager, editText: EditText) {
        campoData = editText
        this.show(supportFragmentManager,"dialogDatePicker")

    }

}