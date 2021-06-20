package br.com.devnattiva.deolhoveiculo.configuration

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
