package com.appdesenvol.monitoraveiculo.logica

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.appdesenvol.monitoraveiculo.R
import com.appdesenvol.monitoraveiculo.logica.AdapterRecyclerView.*
import com.appdesenvol.monitoraveiculo.logica.ControleDados.STATIC.salvarManutencao
import com.appdesenvol.monitoraveiculo.logica.ControleDados.STATIC.veiculoId
import com.appdesenvol.monitoraveiculo.objetos.Manutencao
import com.appdesenvol.monitoraveiculo.telaStatusManutencao
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


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        //Safe call e funcao let garantindo integridade do codigo impedindo NUll

        holder?.let {

            it.descricaoManutencao?.text = manutencoes[position].tipoManutencao


            it.kmtroca?.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(km: Editable?) {}
                override fun beforeTextChanged(km: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(km: CharSequence?, start: Int, before: Int, count: Int) {
                    manutencoes[position].kmtroca = km.toString()

                }
            })


            it.data?.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(data: Editable?) {}
                override fun beforeTextChanged(data: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(data: CharSequence?, start: Int, before: Int, count: Int) {
                    manutencoes[position].data = data.toString()
                }
            })


            it.custo?.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(custo: Editable?) {}
                override fun beforeTextChanged(custo: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(custo: CharSequence?, start: Int, before: Int, count: Int) {
                    manutencoes[position].custo = custo.toString()

                }

            })

            it.kmtroca.setText(manutencoes.get(position).kmtroca)

        }
         alteracaoRecycler(position,manutencoes)


        holder.btSalvar.setOnClickListener {

            if (veiculoId != 0L) {

                val manutencao = Manutencao(
                    0,
                    veiculoId,
                    manutencoes[position].tipoManutencao,
                    manutencoes[position].kmtroca,
                    manutencoes[position].data,
                    manutencoes[position].custo
                )

                salvarManutencao(manutencao, context)

            } else {

                Toast.makeText(context.applicationContext, "SELECIONE O VEICULO", Toast.LENGTH_SHORT).show()


            }
        }

    }

    fun alteracaoRecycler(position:Int,manutencao:ArrayList<Manutencao>){

        notifyItemChanged(position,manutencao.size)


    }


  inner class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        val descricaoManutencao = itemview.tipoManutencao
        var kmtroca = itemview.kmTroca
        var data = itemview.dataTroca
        var custo = itemview.custo
        val btSalvar = itemview.btsalvar2


        init {

            data.addTextChangedListener(ControleDados.mascEdittext("##/##/####", data))
            custo.addTextChangedListener(ControleDados.mascMonetario(custo))

        }

        /*fun bindView(manutencoes : Manutencao){

           val tipoManutencao = itemView.tipoManutencao

           tipoManutencao.hint = manutencoes.tipoManutencao

       }*/
    }

}
