package com.appdesenvol.monitoraveiculo.logica

import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import com.appdesenvol.monitoraveiculo.bancodados.BancoDadoConfig
import com.appdesenvol.monitoraveiculo.model.VeiculoManutencao
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment.*
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.util.Log
import com.opencsv.CSVWriter
import com.opencsv.bean.ColumnPositionMappingStrategy
import com.opencsv.bean.CsvToBeanBuilder
import com.opencsv.bean.StatefulBeanToCsvBuilder
import java.io.*
import java.lang.Exception

@RequiresApi(Build.VERSION_CODES.O)
class ControleBackupRestaurar {

    private lateinit var bd: BancoDadoConfig
    @JvmField
    val ARQUIVO_BKP_CODE = 100
    @JvmField
    val NOME_ARQUIVO = "monitora_veiculo_bkp.csv"
    @JvmField
    val NOME_DIRETORIO = "MonitoraVeiculoBackup"


    fun backupDados(context: Activity) {

         CoroutineScope(IO).launch {
              val dadosBackup = async { buscarDadosParaBkP(context) }
              val fazerBackup = async { criarBackup(context, dadosBackup.await()) }
              fazerBackup.await()

         }

    }

   private fun buscarDadosParaBkP(context: Activity): List<VeiculoManutencao> {
        bd = BancoDadoConfig.getInstance(context.applicationContext)
        return bd.controleDAO().buscaDadosBKP()

   }

   private fun criarBackup(context: Activity, dados: List<VeiculoManutencao>) {

       var arquivoEstrica: FileWriter?= null
       lateinit var diretorioArquivo: String

       if(!permissoes(context, "ESCRITA")) {
           telaPermissoes(context, 1)

       }else {
           try {
               val diretorio = File("${getExternalStorageDirectory()}/"+NOME_DIRETORIO)
               if(!diretorio.mkdir()) {
                   diretorio.mkdir()
               }

               diretorioArquivo = "$diretorio/$NOME_ARQUIVO"
               arquivoEstrica = FileWriter(diretorioArquivo)

               val arquivoBackup = mapeamentoBackupCSV()
               val arquivoCSV = StatefulBeanToCsvBuilder<VeiculoManutencao>(arquivoEstrica)
                   .withMappingStrategy(arquivoBackup)
                   .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                   .withSeparator('\t')
                   .build()

               arquivoCSV.write(dados)

           } catch (e: Exception) {
               Log.e("ERRO_ESCRITA", "ERRO_BACKUP " + e)

           } finally {
               arquivoEstrica?.flush()
               arquivoEstrica?.close()
               enviarArquivoBKP(context, Uri.parse(diretorioArquivo))

           }

       }

   }

   private fun enviarArquivoBKP(context: Activity, diretorioArquivo: Uri) {
         val intent = Intent().apply {
             action = Intent.ACTION_SEND
             putExtra(Intent.EXTRA_STREAM, diretorioArquivo)
             type = "text/csv"

        }
        context.startActivity(Intent.createChooser(intent, "backup..."))
   }
    
    fun restaurarBackupDados(context: Activity) {

        if(!permissoes(context, "LEITURA")) {
            telaPermissoes(context, 2)

        } else {
            context.startActivityForResult(Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "text/csv"
                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("text/*"))
            }, ARQUIVO_BKP_CODE)
        }

    }

    fun arquivoSelecionadoRestaurar(context: Activity, codigoRequisicao: Int, codigoResultado: Int, intent: Intent?) {
        if (codigoRequisicao == ARQUIVO_BKP_CODE && codigoResultado == Activity.RESULT_OK) {
            intent?. data?.also { uri ->
                CoroutineScope(IO).launch {
                    val dadosBackup = async { prepararArquivoRestaurar(context, uri) }
                    //inserirDadosBKP(context, dadosBackup.await())

                }
                
            }
        }
    }

    private fun prepararArquivoRestaurar(context: Activity, arquivoDiretorioUri: Uri): List<VeiculoManutencao> {
        var diretorioLeitura: BufferedReader?= null
        var restauraBackup = listOf<VeiculoManutencao>()

        try {
            diretorioLeitura = context.contentResolver.openInputStream(arquivoDiretorioUri)?.bufferedReader()
            val arquivoRestauraBackupMap = mapeamentoBackupCSV()

            restauraBackup = CsvToBeanBuilder<VeiculoManutencao>(diretorioLeitura)
                .withMappingStrategy(arquivoRestauraBackupMap)
                .withIgnoreLeadingWhiteSpace(true)
                .withSeparator('\t')
                .build()
                .parse()

        }catch (e: Exception) {
            Log.e("ERRO_LEITURA", "ERRO_LEITURA_BACKUP " + e)

        }finally {
            diretorioLeitura?.close()

        }
        return restauraBackup

    }

    private suspend fun inserirDadosBKP(context: Activity, veiculoManutencao: List<VeiculoManutencao>) {
        bd = BancoDadoConfig.getInstance(context.applicationContext)

        if(!veiculoManutencao.isNullOrEmpty()) {
            veiculoManutencao.forEach { bk ->
                bd.controleDAO().salvarDadosBKP(bk.veiculo, bk.manutencao)

            }
        }
    }

    private fun mapeamentoBackupCSV(): ColumnPositionMappingStrategy<VeiculoManutencao> {
        val mapearClassCSV = ColumnPositionMappingStrategy<VeiculoManutencao>()
        mapearClassCSV.type = VeiculoManutencao::class.java
        mapearClassCSV.setColumnMapping(
            "idV", "nomeVeiculo", "marcaVeiculo", "placaVeiculo", "motor",
            "combustivel", "tipoCambio", "ano", "idM", "idVM", "tipoManutencao",
            "kmtroca", "data", "custo", "observacao")
        return mapearClassCSV
    }

    private fun permissoes(context: Activity, escolherPermissao: String): Boolean {
      return when(escolherPermissao) {
          "ESCRITA" -> {
              ContextCompat.checkSelfPermission(context,
                  Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

          }
          "LEITURA" -> {
              ContextCompat.checkSelfPermission(context,
                  Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
          }
          else -> false
      }

  }

  private fun telaPermissoes(context: Activity, codigoPermissao: Int) {
      when (codigoPermissao) {
          1 -> ActivityCompat.requestPermissions(context,
              arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), codigoPermissao)
          2 -> ActivityCompat.requestPermissions(context,
              arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), codigoPermissao)
      }


  }

}


