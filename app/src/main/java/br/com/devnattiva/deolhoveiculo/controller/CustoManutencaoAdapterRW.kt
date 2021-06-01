package br.com.devnattiva.deolhoveiculo.controller

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import br.com.devnattiva.deolhoveiculo.configuration.Util
import br.com.devnattiva.deolhoveiculo.databinding.ManutencoesCustoBinding
import br.com.devnattiva.deolhoveiculo.model.Manutencao

class CustoManutencaoAdapterRW(private val manutencoes: List<Manutencao>,
                               private val context: Context): Adapter<CustoManutencaoAdapterRW.ViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
          ViewHolder(ManutencoesCustoBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun getItemCount(): Int = manutencoes.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.let {
            it.descricaoManutencao.text = manutencoes[it.bindingAdapterPosition].tipoManutencao
            it.data.text = Util.converteDataTexto(manutencoes[it.bindingAdapterPosition].data)
            it.custo.text = manutencoes[it.bindingAdapterPosition].custo
        }
    }

    inner class ViewHolder(itemview: ManutencoesCustoBinding): RecyclerView.ViewHolder(itemview.root) {
          val descricaoManutencao = itemview.txtManutencaoRc
          val data = itemview.txtDataRc
          val custo = itemview.txtCustoRc

        init {
            custo.addTextChangedListener(Util.mascMonetario(custo))
            descricaoManutencao.movementMethod = ScrollingMovementMethod()
            custo.movementMethod = ScrollingMovementMethod()

        }

    }

}
