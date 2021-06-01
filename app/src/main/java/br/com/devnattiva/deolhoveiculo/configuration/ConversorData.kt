package br.com.devnattiva.deolhoveiculo.configuration

import androidx.room.TypeConverter
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class ConversorData {

        @TypeConverter
        fun textoParaData(data: String?): LocalDate? {
            if (!data.isNullOrEmpty()) {
                return LocalDate.parse(data)
            } else return null
        }

        @TypeConverter
        fun dataParaTexto(data: LocalDate?): String? {
            return data?.format(DateTimeFormatter.ISO_LOCAL_DATE)
        }

}