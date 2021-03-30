package com.appdesenvol.monitoraveiculo.logica

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appdesenvol.monitoraveiculo.R
import com.appdesenvol.monitoraveiculo.logica.AdapterRecyclerView.*
import com.appdesenvol.monitoraveiculo.logica.Util.STATIC.veiculoId
import com.appdesenvol.monitoraveiculo.model.Manutencao
import kotlinx.android.synthetic.main.tipo_manutencao.view.*
import kotlin.collections.ArrayList


class AdapterRecyclerView(
    private val manutencoes: ArrayList<Manutencao>,
    private val context: Context
) : Adapter<ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val view = LayoutInflater.from(context).inflate(R.layout.tipo_manutencao, parent, false)


        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return manutencoes.size
    }

    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        //Safe call e funcao let garantindo integridade do codigo impedindo NUll
        holder.let {

            it.descricaoManutencao.text = manutencoes[position].tipoManutencao

            it.kmtroca.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(km: Editable?) {}
                override fun beforeTextChanged(km: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(km: CharSequence?, start: Int, before: Int, count: Int) {
                        manutencoes[it.adapterPosition].kmtroca = km.toString()
                }
            })


            it.data.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(data: Editable?) {}
                override fun beforeTextChanged(data: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(data: CharSequence?, start: Int, before: Int, count: Int) {
                        manutencoes[it.adapterPosition].data = data.toString()
                }
            })


            it.custo.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(custo: Editable?) {}
                override fun beforeTextChanged(custo: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(custo: CharSequence?, start: Int, before: Int, count: Int) {
                       manutencoes[it.adapterPosition].custo = custo.toString()
                }
            })

            it.observacao.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(custo: Editable?) {}
                override fun beforeTextChanged(custo: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(observacao: CharSequence?, start: Int, before: Int, count: Int) {
                        manutencoes[it.adapterPosition].observacao = observacao.toString()
                }
            })

            //Exibe os valores no EditText para o usuário
              it.kmtroca.setText(manutencoes[position].kmtroca)
              it.data.setText(manutencoes[position].data)
              it.custo.setText(manutencoes[position].custo)
              it.observacao.setText(manutencoes[position].observacao)

        }

        holder.btSalvar.setOnClickListener {
            ControleManutencao().salvarManutencao(manutencaoSelecionada(position), context)
        }

        holder.btExcluir.setOnClickListener {
            ControleManutencao().excluirManutencao(manutencaoSelecionada(position), context, holder)

        }

    }

    private fun manutencaoSelecionada(position: Int): Manutencao =
        Manutencao(manutencoes[position].idM,
            veiculoId,
            manutencoes[position].tipoManutencao,
            manutencoes[position].kmtroca,
            manutencoes[position].data,
            manutencoes[position].custo,
            manutencoes[position].observacao)


  inner class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        val descricaoManutencao = itemview.tipoManutencao
        val kmtroca = itemview.kmTroca
        val data = itemview.dataTroca
        val custo = itemview.custo
        val observacao = itemview.m_observacao
        val btSalvar = itemview.btsalvar2
        val btExcluir = itemview.btexcluir_manutencao


        init {

            data.addTextChangedListener(Util.mascEdittext("##/##/####", data))
            custo.addTextChangedListener(Util.mascMonetario(custo))

        }

        /*fun bindView(manutencoes : Manutencao){

           val tipoManutencao = itemView.tipoManutencao

           tipoManutencao.hint = manutencoes.tipoManutencao

       }*/
    }
    

}
