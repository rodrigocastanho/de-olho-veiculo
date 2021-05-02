package com.appdesenvol.monitoraveiculo

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.ArrayAdapter
import com.appdesenvol.monitoraveiculo.repository.BancoDadoConfig
import com.appdesenvol.monitoraveiculo.model.Veiculo
import kotlinx.android.synthetic.main.sobre_veiculo_dialog.view.*

class SobreVeiculoDialog : DialogFragment() {

    private lateinit var veiculo: Veiculo
    private lateinit var bd: BancoDadoConfig

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val dialogSobreVeiculo = AlertDialog.Builder(it)
            val view = requireActivity().layoutInflater.inflate(R.layout.sobre_veiculo_dialog, null)

            bd = BancoDadoConfig.getInstance(it)

            view.sb_txt_nome_veiculo.text = veiculo.nomeVeiculo
            view.sb_txt_marca.text = veiculo.marcaVeiculo
            view.sb_txt_placa.text = veiculo.placaVeiculo
            view.sb_txt_motor.text = veiculo.motor
            view.sb_combustivel.setSelection(ArrayAdapter.createFromResource(it, R.array.combustivel, R.layout.sobre_veiculo_dialog).getPosition(veiculo.combustivel))
            view.sb_combustivel.isEnabled = false
            view.sb_cambio.setSelection(ArrayAdapter.createFromResource(it, R.array.cambio, R.layout.sobre_veiculo_dialog).getPosition(veiculo.tipoCambio))
            view.sb_cambio.isEnabled = false
            view.sb_txt_ano_fabricacao.text = veiculo.ano

            view.bt_editarVeiculo.setOnClickListener {

               val veiculoEditado = Veiculo(veiculo.idV,
                    view.sb_txt_nome_veiculo.text.toString(),
                    view.sb_txt_marca.text.toString(),
                    view.sb_txt_placa.text.toString(),
                    view.sb_txt_motor.text.toString(),
                    view.sb_combustivel.selectedItem.toString(),
                    view.sb_cambio.selectedItem.toString(),
                    view.sb_txt_ano_fabricacao.text.toString())

                   startActivity(Intent(requireContext(), TelaCadastro::class.java)
                       .putExtra("veiculoEditado", veiculoEditado))

            }
            view.bt_fecharVeiculo.setOnClickListener { dialog.dismiss() }

            dialogSobreVeiculo.setTitle("                        Veículo")
            dialogSobreVeiculo.setView(view)
            dialogSobreVeiculo.create()

        } ?: throw IllegalStateException("Erro ao exibir tela de veículo")

    }

    fun veiculoSelecionado(veiculoSelecionado: Veiculo) {
        this.veiculo = veiculoSelecionado
    }

}






