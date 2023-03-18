package br.com.devnattiva.deolhoveiculo.configuration

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class ConversorData {

        @TypeConverter
        fun textoParaData(data: String?): Date? {
            return Util.converteTextoData(data)
        }

        @TypeConverter
        fun dataParaTexto(data: Date?): String? {
            return Util.converteDataTexto(data)
        }
}
