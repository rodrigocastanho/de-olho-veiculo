package br.com.devnattiva.deolhoveiculo.controller

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.devnattiva.deolhoveiculo.AddManutencaoDialog
import br.com.devnattiva.deolhoveiculo.configuration.Util
import br.com.devnattiva.deolhoveiculo.controller.StatusManutencaoAdapterRW.ViewHolder
import br.com.devnattiva.deolhoveiculo.databinding.TipoManutencaoBinding
import br.com.devnattiva.deolhoveiculo.model.Manutencao

class StatusManutencaoAdapterRW(
    private val context: Activity,
    private val fragment: FragmentManager,
    private val controleManutencao: ControleManutencao
) : ListAdapter<Manutencao, ViewHolder>(diffCallback) {

    companion object {
        private val diffCallback = object: DiffUtil.ItemCallback<Manutencao>() {
            override fun areItemsTheSame(oldItem: Manutencao, newItem: Manutencao): Boolean {
                return verificaItem(oldItem, newItem)
            }

            override fun areContentsTheSame(oldItem: Manutencao, newItem: Manutencao): Boolean {
                return verificaItem(oldItem, newItem)
            }
        }

        private fun verificaItem(antesItem: Manutencao, depoisItem: Manutencao): Boolean {
            return antesItem.idM == depoisItem.idM &&
                    antesItem.idVM == depoisItem.idVM &&
                    antesItem.tipoManutencao == depoisItem.tipoManutencao &&
                    antesItem.kmtroca == depoisItem.kmtroca &&
                    antesItem.kmtrocaAtual == depoisItem.kmtrocaAtual &&
                    antesItem.data?.compareTo(depoisItem.data) == 0 &&
                    antesItem.custo == depoisItem.custo &&
                    antesItem.observacao == depoisItem.observacao
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ) = ViewHolder(
        TipoManutencaoBinding
            .inflate(LayoutInflater.from(context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.inserirDado(getItem(position))
    }

  inner class ViewHolder(
      binding: TipoManutencaoBinding
  ) : RecyclerView.ViewHolder(binding.root) {

      private val tipoManutencao = binding.tipoManutencao
      private val kmAtual = binding.tvKmAtual
      private val kmFinal = binding.tvKmFinal
      private val dataManutencao = binding.tvData
      private val custo = binding.tvCusto
      private val observacao = binding.tvObs
      private val botaoEditar = binding.ivEditar
      private val botaoDeletar = binding.ivDeletar

      fun inserirDado(item: Manutencao) {
          tipoManutencao.text = item.tipoManutencao
          kmAtual.text = item.kmtrocaAtual
          kmFinal.text = item.kmtroca
          dataManutencao.text = Util.converteDataTexto(item.data)
          custo.text = item.custo
          observacao.text = item.observacao

          onClickControle(item)
      }

      private fun onClickControle(manutencao: Manutencao) {
          botaoEditar.setOnClickListener {
              AddManutencaoDialog(
                  context = context,
                  manutencao = manutencao,
                  fragmentManager = fragment
              ).createDialog(
                  primaryButtonAction = { manutencao, dialog ->
                      dialog.dismiss()
                      controleManutencao.salvarManutencao(manutencao, context)
                      controleManutencao.atualizarManutencao(manutencao, bindingAdapterPosition)
                      this@StatusManutencaoAdapterRW.submitList(controleManutencao.manutencoes)
                  },
                  secundaryButtonAction = { dialog -> dialog.dismiss()}
              )
          }

          botaoDeletar.setOnClickListener {
              controleManutencao.deletarManutencao(
                  manutencao,
                  context,
                  callBack = {
                      controleManutencao.deletarManutencao(manutencao)
                      this@StatusManutencaoAdapterRW.submitList(controleManutencao.manutencoes)
                  })
          }
      }
  }
}
