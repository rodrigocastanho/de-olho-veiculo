package com.appdesenvol.monitoraveiculo.logica


import android.content.Context
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.Toast
import com.appdesenvol.monitoraveiculo.bancodados.BancoDadoConfig
import com.appdesenvol.monitoraveiculo.model.Manutencao
import com.appdesenvol.monitoraveiculo.telaStatusManutencao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.SQLException
import kotlin.collections.ArrayList


class ControleManutencao: Manutencoes {

    val arrayManutencoes = ArrayList<Manutencao>()
    private lateinit var bd: BancoDadoConfig

    init {
          arrayManutencoes.addAll(manutencoesConfig())

    }

    fun resultManutencao(manutencao: Manutencao) {

        for (indx in 0 until arrayManutencoes.size) {
            if(manutencao.tipoManutencao.contentEquals(arrayManutencoes[indx].tipoManutencao)) {

                arrayManutencoes[indx] = manutencao

                Log.i("INDX ", " INDX " + indx)
                Log.i("KMTROCA ", " BD " + manutencao.tipoManutencao)
                Log.i("KMTROCA ", " ARRAY " + arrayManutencoes[indx].tipoManutencao)
                Log.i("KMTROCA ", " BD " + manutencao.kmtroca)

            }
        }

    }

    fun salvarManutencao(manutencao: Manutencao, context: Context) {

         bd = BancoDadoConfig.getInstance(context.applicationContext)

        if(manutencao.idVM != 0L) {
            if (manutencao.kmtroca.isNotEmpty() || manutencao.data.isNotEmpty() || manutencao.custo.isNotEmpty()) {

                CoroutineScope(Dispatchers.IO).launch {
                    try {

                        when (manutencao.idM) {
                            0L -> bd.controleDAO().salvarDadosManutencao(manutencao)
                            else -> bd.controleDAO().alterarDadosManutencao(manutencao)
                        }

                    } catch (e: SQLException) {
                        Log.e("ERRO Manutencao insert", " VERIFICAR ERRO: " + e)

                    }

                }
                Toast.makeText(context,
                    " MANUTENÇÃO GRAVADA " + manutencao.tipoManutencao.toUpperCase(),
                    Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(context,
                    "ADICIONE PELO MENOS UMA INFORMAÇÃOS EM UM DOS CAMPOS: " + manutencao.tipoManutencao.toUpperCase(),
                    Toast.LENGTH_LONG).show()
            }

        } else {
            Toast.makeText(context,"SELECIONE O VEÌCULO", Toast.LENGTH_SHORT).show()
        }

    }

    fun excluirManutencao(manutencao: Manutencao, context: Context, holder: AdapterRecyclerView.ViewHolder) {

        bd = BancoDadoConfig.getInstance(context.applicationContext)

        if(manutencao.idM != 0L) {

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Excluir manutenção")
            builder.setMessage("Deseja excluir essa manutenção?")
            builder.setPositiveButton("SIM") { _, _ ->

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        bd.controleDAO().deletarDadosManutencao(manutencao)

                    } catch (e: SQLException) {
                        Log.e("ERRO_DEL_MANUTENCAO", "ERRO_DELETAR_MANUTECAO: " + e)
                    }
                }
                Toast.makeText(context, "MANUTENÇÃO EXCLUIDA", Toast.LENGTH_SHORT).show()

                //limpo os campos depois de excluir dados da manutenção
                holder.kmtroca.setText("")
                holder.data.setText("")
                holder.custo.setText("")
                holder.observacao.setText("")

            }
            builder.setNegativeButton("NÃO") { _, _ ->
                Toast.makeText(context, "MANUTENÇÃO NÃO SERÁ EXCLUIDA", Toast.LENGTH_SHORT).show()

            }

            builder.setNeutralButton("CANCELAR") { _, _ -> }
            val dialog: AlertDialog = builder.create()
            dialog.show()

        } else {
            Toast.makeText(context, "NÃO POSSUI MANUTENÇÃO", Toast.LENGTH_SHORT).show()
        }

    }

    fun limparArrayManutencoes() {

        arrayManutencoes.forEach{m ->
            m.idM = 0
            m.idVM = 0
            m.kmtroca = ""
            m.data = ""
            m.custo = ""
            m.observacao = ""

        }

    }


}