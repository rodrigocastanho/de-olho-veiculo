package br.com.devnattiva.deolhoveiculo.controller

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import br.com.devnattiva.deolhoveiculo.repository.BancoDadoConfig
import br.com.devnattiva.deolhoveiculo.model.VeiculoManutencao
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.core.app.ActivityCompat
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import br.com.devnattiva.deolhoveiculo.configuration.Util
import br.com.devnattiva.deolhoveiculo.databinding.ActivityTelaBackupBinding
import br.com.devnattiva.deolhoveiculo.model.Manutencao
import br.com.devnattiva.deolhoveiculo.model.Veiculo
import com.opencsv.CSVReaderBuilder
import com.opencsv.CSVWriter
import kotlinx.coroutines.Dispatchers.Main
import java.io.*
import kotlin.Exception

class ControleBackupRestaurar {

    private lateinit var bd: BancoDadoConfig
    @JvmField
    val NOME_ARQUIVO = "deolhoveiculo_bkp.csv"
    @JvmField
    val NOME_DIRETORIO = "DeOlhoVeiculoBackup"


    fun backupDados(context: Activity, viewActivityBackup: ActivityTelaBackupBinding) {
        exibirProcessoCarramento( true, "", viewActivityBackup)
         CoroutineScope(IO).launch {
              val dadosBackup = async { buscarDadosParaBkP(context) }
              if(!dadosBackup.await().isNullOrEmpty()) {
               val status = criarBackup(context, dadosBackup.await())
                  withContext(Main) {
                      if(status) {
                          exibirProcessoCarramento(false,"Backup criado com sucesso", viewActivityBackup)
                      }else {
                          Toast.makeText(context,"BACKUP NÃO FOI POSSÍVEL SER CRIADO", Toast.LENGTH_LONG).show()
                          exibirProcessoCarramento(false, "", viewActivityBackup)
                      }
                  }
              }else {
                  withContext(Main) {
                      exibirProcessoCarramento( false, "Não há dados cadastrados", viewActivityBackup)
                  }
              }

         }

    }

   private fun buscarDadosParaBkP(context: Activity): List<VeiculoManutencao> {
        bd = BancoDadoConfig.getInstance(context.applicationContext)
        return bd.controleDAO().buscaDadosBKP()

   }

   private fun criarBackup(context: Activity, dados: List<VeiculoManutencao>): Boolean {

       var arquivoEstrica: FileWriter?= null
       var status = false
       lateinit var diretorioArquivo: String

       if(!permissoes(context, "ESCRITA")) {
           telaPermissoes(context, 1)

       }else {
           try {
               val diretorio = File(context.getExternalFilesDir(null), NOME_DIRETORIO)
               if(!diretorio.exists()) {
                   diretorio.mkdir()
               }

               diretorioArquivo = "$diretorio/$NOME_ARQUIVO"
               arquivoEstrica = FileWriter(diretorioArquivo)

               val arquivoCSV = CSVWriter(arquivoEstrica,
                                           ',',
                                            CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                                            CSVWriter.NO_ESCAPE_CHARACTER,
                                            CSVWriter.DEFAULT_LINE_END)

              dados.forEach { d ->
                 val backupsDados = arrayOf(d.veiculo.idV.toString(),
                     d.veiculo.nomeVeiculo, d.veiculo.marcaVeiculo,
                     d.veiculo.cor, d.veiculo.placaVeiculo, d.veiculo.motor,
                     d.veiculo.combustivel, d.veiculo.tipoCambio,
                     d.veiculo.ano, d.manutencao.idM.toString(),
                     d.manutencao.idVM.toString(), d.manutencao.tipoManutencao,
                     d.manutencao.kmtrocaAtual, d.manutencao.kmtroca,
                     Util.converteDataTexto(d.manutencao.data),
                     d.manutencao.custo, d.manutencao.observacao)

                 arquivoCSV.writeNext(backupsDados)

              }

//           Nas API anteriorior 24 esse trecho de código não funciona pois há incompatibilidades Beans e Mapper
//               val arquivoBackup = mapeamentoBackupCSV()
//               val arquivoCSV = StatefulBeanToCsvBuilder<VeiculoManutencao>(arquivoEstrica)
//                   .withMappingStrategy(arquivoBackup)
//                   .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
//                   .withSeparator('\t')
//                   .build()
//
//               arquivoCSV.write(dados)

               status = true
           } catch (e: Exception) {
               status = false
               Log.e("ERRO_ESCRITA", "ERRO_BACKUP $e")

           } finally {
               arquivoEstrica?.flush()
               arquivoEstrica?.close()
               if(status) {
                   enviarArquivoBKP(context, Uri.parse(diretorioArquivo))
               }
           }

       }
       return status

   }

   private fun enviarArquivoBKP(context: Activity, diretorioArquivo: Uri) {
         val intent = Intent().apply {
             action = Intent.ACTION_SEND
             putExtra(Intent.EXTRA_STREAM, diretorioArquivo)
             type = "text/csv"

        }
        context.startActivity(Intent.createChooser(intent, "backup..."))
   }
    
    fun restaurarBackupDados(context: Activity, arquivo: ActivityResultLauncher<Intent>) {
        if(!permissoes(context, "LEITURA")) {
            telaPermissoes(context, 2)

        } else {
            arquivo.launch(
                Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "text/csv"
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("text/*"))
                })

        }

    }

    fun arquivoSelecionadoRestaurar(context: Activity, codigoResultado: Int, intent: Intent?, viewActivityBackup: ActivityTelaBackupBinding ) {
        if (codigoResultado == Activity.RESULT_OK) {
            var status: Boolean
            exibirProcessoCarramento( true, "", viewActivityBackup)

            intent?. data?.also { uri ->
                CoroutineScope(IO).launch {
                    val dadosBackup = async { prepararArquivoRestaurar(context, uri) }
                    status = inserirDadosBKP(context, dadosBackup.await())

                    if(status) {
                        withContext(Main) {
                            exibirProcessoCarramento( false, "Backup restaurado com sucesso", viewActivityBackup)
                        }
                    }else {
                        withContext(Main) {
                            Toast.makeText(context,"ARQUIVO ESTA COM INCOMPATIBILIDADE NOS DADOS", Toast.LENGTH_LONG).show()
                            exibirProcessoCarramento(false, "", viewActivityBackup)
                        }
                    }

                }
            }

        }
    }

    private fun prepararArquivoRestaurar(context: Activity, arquivoDiretorioUri: Uri): List<VeiculoManutencao> {
        var diretorioLeitura: BufferedReader?= null
        val restauraBackup = mutableListOf<VeiculoManutencao>()

        try {
            diretorioLeitura = context.contentResolver.openInputStream(arquivoDiretorioUri)?.bufferedReader()
            val arquivoRestaurado = CSVReaderBuilder(diretorioLeitura).build()

            arquivoRestaurado.readAll().forEach { l ->
                val dados = VeiculoManutencao(
                    Veiculo(l[0].toLong(), l[1], l[2], l[3], l[4], l[5], l[6], l[7], l[8]),
                    Manutencao(l[9].toLong(), l[10].toLong(), l[11], l[12], l[13],
                        Util.converteTextoData(l[14]), l[15], l[16]))

                restauraBackup.add(dados)

            }

//           Nas API anteriorior 24 esse trecho de código não funciona pois há incompatibilidades Beans e Mapper
//            diretorioLeitura = context.contentResolver.openInputStream(arquivoDiretorioUri)?.bufferedReader()
//            val arquivoRestauraBackupMap = mapeamentoBackupCSV()
//            restauraBackup = CsvToBeanBuilder<VeiculoManutencao>(diretorioLeitura)
//                .withMappingStrategy(arquivoRestauraBackupMap)
//                .withIgnoreLeadingWhiteSpace(true)
//                .withSeparator('\t')
//                .withExceptionHandler { CsvDataTypeMismatchException() }
//                .build()
//                .parse()

        }catch (e: Exception) {
            Log.e("ERRO_LEITURA", "ERRO_LEITURA_BACKUP $e")
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
        } finally {
            bd.close()
        }
        return status

    }

 //   metodo Mapper utilizado, funciona nas API acima da 24 android
//    private fun mapeamentoBackupCSV(): ColumnPositionMappingStrategy<VeiculoManutencao> {
//        val mapearClassCSV = ColumnPositionMappingStrategy<VeiculoManutencao>()
//        mapearClassCSV.type = VeiculoManutencao::class.java
//        mapearClassCSV.setColumnMapping(
//            "idV", "nomeVeiculo", "marcaVeiculo", "placaVeiculo", "motor",
//            "combustivel", "tipoCambio", "ano", "idM", "idVM", "tipoManutencao",
//            "kmtroca", "data", "custo", "observacao")
//        return mapearClassCSV
//    }

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

    private fun exibirProcessoCarramento(processo: Boolean, mensagem: String, viewActivityBackup: ActivityTelaBackupBinding) {
        if(processo) {
            viewActivityBackup.txtBkRtCompleto.visibility = View.GONE
            viewActivityBackup.ldBackup.visibility = View.VISIBLE
        } else {
            viewActivityBackup.ldBackup.visibility = View.GONE
            viewActivityBackup.txtBkRtCompleto.visibility = View.VISIBLE
            viewActivityBackup.txtBkRtCompleto.text = mensagem
        }

    }

}


