package br.com.devnattiva.deolhoveiculo.controller


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.appcompat.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import br.com.devnattiva.deolhoveiculo.SobreVeiculoDialog
import br.com.devnattiva.deolhoveiculo.databinding.ContentTelaStatusManutencaoBinding
import br.com.devnattiva.deolhoveiculo.databinding.EditarTipoManutencaoDialogBinding
import br.com.devnattiva.deolhoveiculo.repository.BancoDadoConfig
import br.com.devnattiva.deolhoveiculo.model.Manutencao
import br.com.devnattiva.deolhoveiculo.model.Veiculo
import br.com.devnattiva.deolhoveiculo.model.VeiculoManutencao
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import java.sql.SQLException
import java.util.*


class ControleManutencao: Manutencoes {

    val arrayManutencoes = LinkedList<Manutencao>()
    private lateinit var bd: BancoDadoConfig

    init {
        this.arrayManutencoes
    }

    override fun manutencoesConfig(manutencao: Manutencao): LinkedList<Manutencao> {
        arrayManutencoes.addFirst(manutencao)
        return arrayManutencoes

    }

    @SuppressLint("NotifyDataSetChanged")
    fun fluxoManutencao(veiculoId: Long, context: Activity, supportFragmentManager: FragmentManager, viewConteudo: ContentTelaStatusManutencaoBinding) {
        lateinit var veiculo : Veiculo
        bd = BancoDadoConfig.getInstance(context.applicationContext)

        if(veiculoId != 0L) {
            arrayManutencoes.clear()
            CoroutineScope(Dispatchers.IO).launch {

                try {

                    veiculo = bd.controleDAO().buscaVeiculoId(veiculoId)

                    val buscveiculomanutencao = bd.controleDAO().buscaVeiculoManutencao(veiculoId)

                    if(buscveiculomanutencao.isNullOrEmpty()) {
                        manutencoesConfig(addManutencao())

                    }else {
                        for (vm: VeiculoManutencao in buscveiculomanutencao) {
                            manutencoesConfig(vm.manutencao)

                        }

                    }
                    withContext(Main) {
                        viewConteudo.recyclerViewManutencao.adapter?.notifyDataSetChanged()
                    }
                } catch (e: Exception) {
                    Log.e("ERRO_VEICULO_M", "ERRO_VEICULO_MANUTENÇÂO $e")

                } finally {
                    bd.close()
                }

            }

            viewConteudo.btInfoVeiculo.isEnabled = true
            viewConteudo.btInfoVeiculo.setOnClickListener {
                val sobreVeiculoDialog = SobreVeiculoDialog()
                sobreVeiculoDialog.veiculoSelecionado(veiculo)
                sobreVeiculoDialog.show(supportFragmentManager, "dialogSobreVeiculo")

            }

            if(viewConteudo.recyclerViewManutencao.visibility == View.GONE) {
                viewConteudo.txInicioManutencao.visibility = View.GONE
                viewConteudo.recyclerViewManutencao.visibility = View.VISIBLE
                viewConteudo.btFtAddManutencao.show()
            }

        }else {
            viewConteudo.btInfoVeiculo.isEnabled = false
            viewConteudo.txInicioManutencao.visibility = View.VISIBLE
            viewConteudo.recyclerViewManutencao.visibility = View.GONE
            viewConteudo.btFtAddManutencao.hide()
        }

    }

    fun salvarManutencao(manutencao: Manutencao, context: Context) {

         bd = BancoDadoConfig.getInstance(context.applicationContext)

        if(manutencao.idVM != 0L) {
            if (manutencao.kmtrocaAtual.isNotEmpty() ||
                manutencao.kmtroca.isNotEmpty() ||
                manutencao.data != null ||
                manutencao.custo.isNotEmpty()
            ) {

                CoroutineScope(Dispatchers.IO).launch {
                    try {

                        when (manutencao.idM) {
                            0L -> bd.controleDAO().salvarDadosManutencao(manutencao)
                            else -> bd.controleDAO().alterarDadosManutencao(manutencao)
                        }

                    } catch (e: SQLException) {
                        Log.e("ERRO_MANUTENCAO_INSERIR", "ERRO_MANUTENCAO_INSERIR: $e")

                    } finally {
                        bd.close()
                    }

                }
                Toast.makeText(context,
                    " MANUTENÇÃO GRAVADA " + manutencao.tipoManutencao.uppercase(Locale.ROOT),
                    Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(context,
                    "ADICIONE PELO MENOS UMA INFORMAÇÃOS EM UM DOS CAMPOS: " + manutencao.tipoManutencao.uppercase(
                        Locale.ROOT),
                    Toast.LENGTH_LONG).show()
            }

        } else {
            Toast.makeText(context,"SELECIONE O VEÌCULO", Toast.LENGTH_SHORT).show()
        }

    }

    fun excluirManutencao(manutencao: Manutencao, context: Context, holder: StatusManutencaoAdapterRW.ViewHolder,
                          manutencaoAdapter: StatusManutencaoAdapterRW, manutencoes: LinkedList<Manutencao>) {

        bd = BancoDadoConfig.getInstance(context.applicationContext)

        if(manutencao.idM != 0L || manutencao.tipoManutencao == addManutencao().tipoManutencao) {

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Excluir manutenção")
            builder.setMessage("Deseja excluir essa manutenção?")
            builder.setPositiveButton("SIM") { _, _ ->

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        bd.controleDAO().deletarDadosManutencao(manutencao)

                    } catch (e: SQLException) {
                        Log.e("ERRO_DEL_MANUTENCAO", "ERRO_DELETAR_MANUTECAO: $e")
                    } finally {
                        bd.close()
                    }

                }
                Toast.makeText(context, "MANUTENÇÃO EXCLUIDA", Toast.LENGTH_SHORT).show()

                manutencoes.removeAt(holder.bindingAdapterPosition)
                manutencaoAdapter.notifyItemRemoved(holder.bindingAdapterPosition)

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

    fun editarTipoManutencao(context: Context, holder: StatusManutencaoAdapterRW.ViewHolder) {

        val builder = AlertDialog.Builder(context)
        val viewAlertDialog = EditarTipoManutencaoDialogBinding.inflate(LayoutInflater.from(context))
        builder.setPositiveButton("EDITAR") { _,_ ->
            if(viewAlertDialog.edTipoManutencao.text.toString().isNotBlank()) {
                holder.descricaoManutencao.text = viewAlertDialog.edTipoManutencao.text.toString()
            }
        }
        builder.setNeutralButton("FECHAR") { _, _ -> }
        builder.setView(viewAlertDialog.root)
        builder.create()
        builder.show()
        
    }
    
    
}