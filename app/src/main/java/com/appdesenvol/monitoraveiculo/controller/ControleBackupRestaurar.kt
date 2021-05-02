package com.appdesenvol.monitoraveiculo.controller

import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import com.appdesenvol.monitoraveiculo.repository.BancoDadoConfig
import com.appdesenvol.monitoraveiculo.model.VeiculoManutencao
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment.*
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import android.widget.Toast
import com.appdesenvol.monitoraveiculo.model.Manutencao
import com.opencsv.CSVWriter
import com.opencsv.bean.ColumnPositionMappingStrategy
import com.opencsv.bean.CsvToBeanBuilder
import com.opencsv.bean.StatefulBeanToCsvBuilder
import com.opencsv.exceptions.CsvDataTypeMismatchException
import kotlinx.android.synthetic.main.activity_tela_backup.*
import kotlinx.coroutines.Dispatchers.Main
import java.io.*
import kotlin.Exception

@RequiresApi(Build.VERSION_CODES.O)
class ControleBackupRestaurar {

    private lateinit var bd: BancoDadoConfig
    @JvmField
    val ARQUIVO_BKP_CODE = 100
    @JvmField
    val NOME_ARQUIVO = "monitora_veiculo_bkp.csv"
    @JvmField
    val NOME_DIRETORIO = "MonitoraVeiculoBackup"


    @SuppressLint("SetTextI18n")
    fun backupDados(context: Activity) {
        exibirProcessoCarramento(context, true, "")
         CoroutineScope(IO).launch {
              val dadosBackup = async { buscarDadosParaBkP(context) }
              criarBackup(context, dadosBackup.await())
              withContext(Main) {
                  exibirProcessoCarramento(context, false, "Backup criado com sucesso")
               }
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

    @SuppressLint("SetTextI18n")
    fun arquivoSelecionadoRestaurar(context: Activity, codigoRequisicao: Int, codigoResultado: Int, intent: Intent?) {
        if (codigoRequisicao == ARQUIVO_BKP_CODE && codigoResultado == Activity.RESULT_OK) {
            var status: Boolean
            exibirProcessoCarramento(context, true, "")

            intent?. data?.also { uri ->
                CoroutineScope(IO).launch {
                    val dadosBackup = async { prepararArquivoRestaurar(context, uri) }
                    status = inserirDadosBKP(context, dadosBackup.await())

                    if(status) {
                        withContext(Main) {
                            exibirProcessoCarramento(context, false, "Backup restaurado com sucesso")
                        }
                    }else {
                        withContext(Main) {
                            Toast.makeText(context,"ARQUIVO ESTA COM INCOMPATIBILIDADE NOS DADOS", Toast.LENGTH_LONG).show()
                            exibirProcessoCarramento(context, false, "")
                        }
                    }

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
                .withExceptionHandler { CsvDataTypeMismatchException() }
                .build()
                .parse()

        }catch (e: Exception) {
            Log.e("ERRO_LEITURA", "ERRO_LEITURA_BACKUP " + e)
        }finally {
            diretorioLeitura?.close()
        }
        return restauraBackup

    }

    private fun inserirDadosBKP(context: Activity, veiculoManutencao: List<VeiculoManutencao>): Boolean {
        var status: Boolean
        val manutencoes = ArrayList<Manutencao>()
        bd = BancoDadoConfig.getInstance(context.applicationContext)

        try {
            if (veiculoManutencao.component1().veiculo.idV != 0L) {
                status = true
                veiculoManutencao.forEach { bk ->
                    bd.controleDAO().salvarDadosVeiculoBKP(bk.veiculo)
                    manutencoes.add(bk.manutencao)

                }
                bd.controleDAO().salvarDadosManutencaoBKP(manutencoes)
            } else return false
        }catch (e: Exception) {
            status = false
        }
        return status

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

    private fun exibirProcessoCarramento(context: Activity, processo: Boolean, mensagem: String) {
        if(processo) {
            context.txt_bk_rt_completo.visibility = View.GONE
            context.ld_backup.visibility = View.VISIBLE
        } else {
            context.ld_backup.visibility = View.GONE
            context.txt_bk_rt_completo.visibility = View.VISIBLE
            context.txt_bk_rt_completo.text = mensagem
        }

    }

}


