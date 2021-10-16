package br.com.devnattiva.deolhoveiculo.controller

import android.app.Activity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import br.com.devnattiva.deolhoveiculo.DatePickerFragmentDialog
import br.com.devnattiva.deolhoveiculo.R
import br.com.devnattiva.deolhoveiculo.configuration.Util
import br.com.devnattiva.deolhoveiculo.controller.StatusManutencaoAdapterRW.*
import br.com.devnattiva.deolhoveiculo.configuration.Util.STATIC.veiculoId
import br.com.devnattiva.deolhoveiculo.databinding.TipoManutencaoBinding
import br.com.devnattiva.deolhoveiculo.model.Manutencao
import java.util.*


class StatusManutencaoAdapterRW(
    private val manutencoes: LinkedList<Manutencao>,
    private val context: Activity,
    private val supportFragmentManager: FragmentManager
) : Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = TipoManutencaoBinding.inflate(LayoutInflater.from(context), parent, false)

        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return manutencoes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        //Safe call e funcao let garantindo integridade do codigo impedindo NUll
        holder.let {

            it.descricaoManutencao.text = manutencoes[position].tipoManutencao

            it.kmtroca.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(km: Editable?) {}
                override fun beforeTextChanged(km: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(km: CharSequence?, start: Int, before: Int, count: Int) {
                        manutencoes[it.bindingAdapterPosition].kmtroca = km.toString()
                }
            })

           it.data.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(data: Editable?) {}
                override fun beforeTextChanged(data: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(data: CharSequence?, start: Int, before: Int, count: Int) {
                     manutencoes[it.bindingAdapterPosition].data  = Util.converteTextoData(data.toString())
                }
            })

            it.custo.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(custo: Editable?) {}
                override fun beforeTextChanged(custo: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(custo: CharSequence?, start: Int, before: Int, count: Int) {
                       manutencoes[it.bindingAdapterPosition].custo = Util.conversorMonetario(custo.toString())
                }
            })

            it.observacao.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(custo: Editable?) {}
                override fun beforeTextChanged(custo: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(observacao: CharSequence?, start: Int, before: Int, count: Int) {
                        manutencoes[it.bindingAdapterPosition].observacao = observacao.toString()


                }
            })

            //Exibe os valores no EditText para o usuário
              it.kmtroca.setText(manutencoes[position].kmtroca)
              it.data.setText(Util.converteDataTexto(manutencoes[position].data))
              it.custo.setText(manutencoes[position].custo)
              it.observacao.setText(manutencoes[position].observacao)

        }

        holder.btSalvar.setOnClickListener {
            ControleManutencao().salvarManutencao(manutencaoSelecionada(position, holder), context)
        }

        holder.btExcluir.setOnClickListener {
           ControleManutencao().excluirManutencao(manutencaoSelecionada(position, holder), context, holder,
               this, manutencoes)

        }

        holder.btEditarManutencao.setOnClickListener {
            ControleManutencao().editarTipoManutencao(context, holder)

        }


    }

    private fun manutencaoSelecionada(position: Int, holder: ViewHolder): Manutencao =
        Manutencao(manutencoes[position].idM,
            veiculoId,
            holder.descricaoManutencao.text.toString(), //manutencoes[position].tipoManutencao
            manutencoes[position].kmtroca,
            manutencoes[position].data,
            manutencoes[position].custo,
            manutencoes[position].observacao)


  inner class ViewHolder(itemview: TipoManutencaoBinding) : RecyclerView.ViewHolder(itemview.root), View.OnClickListener {

        val descricaoManutencao = itemview.tipoManutencao
        val kmtroca = itemview.kmTroca
        val data = itemview.dataTroca
        val custo = itemview.custo
        val observacao = itemview.mObservacao
        val btSalvar = itemview.btsalvar2
        val btExcluir = itemview.btexcluirManutencao
        val btEditarManutencao = itemview.btEditarTpManutencao


      init {
            data.setOnClickListener {
                 Util.fecharTeclado(context)
                 DatePickerFragmentDialog().exibirDataPicker(supportFragmentManager, data)
            }
            custo.addTextChangedListener(Util.mascMonetario(custo))
            observacao.setOnClickListener(this)

      }

      override fun onClick(v: View?) {
          Util.fecharTeclado(context)
      }

  }

}
