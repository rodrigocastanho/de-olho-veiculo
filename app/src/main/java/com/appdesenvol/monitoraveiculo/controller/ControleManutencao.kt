package com.appdesenvol.monitoraveiculo.controller


import android.app.Activity
import android.content.Context
import android.os.Handler
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.appdesenvol.monitoraveiculo.R
import com.appdesenvol.monitoraveiculo.SobreVeiculoDialog
import com.appdesenvol.monitoraveiculo.repository.BancoDadoConfig
import com.appdesenvol.monitoraveiculo.model.Manutencao
import com.appdesenvol.monitoraveiculo.model.Veiculo
import com.appdesenvol.monitoraveiculo.model.VeiculoManutencao
import kotlinx.android.synthetic.main.content_tela_status_manutencao.*
import kotlinx.android.synthetic.main.editar_tipo_manutencao_dialog.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.SQLException
import kotlin.collections.ArrayList


class ControleManutencao: Manutencoes {

    val arrayManutencoes = ArrayList<Manutencao>()
    private lateinit var bd: BancoDadoConfig

    init {
        this.arrayManutencoes
    }

    override fun manutencoesConfig(manutencao: Manutencao): ArrayList<Manutencao> {
        arrayManutencoes.add(manutencao)
        return arrayManutencoes

    }

    fun fluxoManutencao(veiculoId: Long, context: Activity, supportFragmentManager: FragmentManager) {

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
                } catch (e: Exception) {
                    Log.e("ERRO_VEICULO_M", "ERRO_VEICULO_MANUTENÇÂO" + e)

                }

            }
            Handler().postDelayed({
                context.recyclerViewManutencao.adapter?.notifyDataSetChanged()
            }, 285)
            
            context.bt_infoVeiculo.isEnabled = true
            context.bt_infoVeiculo.setOnClickListener {
                val sobreVeiculoDialog = SobreVeiculoDialog()
                sobreVeiculoDialog.veiculoSelecionado(veiculo)
                sobreVeiculoDialog.show(supportFragmentManager, "dialogSobreVeiculo")

            }

            if(context.recyclerViewManutencao.visibility == View.GONE) {
                context.tx_inicio_manutencao.visibility = View.GONE
                context.recyclerViewManutencao.visibility = View.VISIBLE
                context.bt_ft_add_manutencao.show()
            }

        }else {
            context.bt_infoVeiculo.isEnabled = false
            context.tx_inicio_manutencao.visibility = View.VISIBLE
            context.recyclerViewManutencao.visibility = View.GONE
            context.bt_ft_add_manutencao.hide()
        }

    }

    fun salvarManutencao(manutencao: Manutencao, context: Context) {

         bd = BancoDadoConfig.getInstance(context.applicationContext)

        if(manutencao.idVM != 0L) {
            if (manutencao.kmtroca.isNotEmpty() || !manutencao.data!!.equals(null) || manutencao.custo.isNotEmpty()) {

                CoroutineScope(Dispatchers.IO).launch {
                    try {

                        when (manutencao.idM) {
                            0L -> bd.controleDAO().salvarDadosManutencao(manutencao)
                            else -> bd.controleDAO().alterarDadosManutencao(manutencao)
                        }

                    } catch (e: SQLException) {
                        Log.e("ERRO_MANUTENCAO_INSERIR", "ERRO_MANUTENCAO_INSERIR: " + e)

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

    fun excluirManutencao(manutencao: Manutencao, context: Context, holder: StatusManutencaoAdapterRW.ViewHolder,
                          manutencaoAdapter: StatusManutencaoAdapterRW, manutencoes: ArrayList<Manutencao>) {

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
                        Log.e("ERRO_DEL_MANUTENCAO", "ERRO_DELETAR_MANUTECAO: " + e)
                    }
                }
                Toast.makeText(context, "MANUTENÇÃO EXCLUIDA", Toast.LENGTH_SHORT).show()

                manutencoes.removeAt(holder.adapterPosition)
                manutencaoAdapter.notifyItemRemoved(holder.adapterPosition)

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
        val view = LayoutInflater.from(context).inflate(R.layout.editar_tipo_manutencao_dialog, null)
        builder.setPositiveButton("EDITAR") { _,_ ->
            holder.descricaoManutencao.text = view.ed_tipo_manutencao?.text
        }
        builder.setNeutralButton("FECHAR") { _, _ -> }
        builder.setView(view)
        builder.create()
        builder.show()
        
    }
    
    
}