package br.com.devnattiva.deolhoveiculo

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog
import android.widget.ArrayAdapter
import br.com.devnattiva.deolhoveiculo.databinding.SobreVeiculoDialogBinding
import br.com.devnattiva.deolhoveiculo.repository.BancoDadoConfig
import br.com.devnattiva.deolhoveiculo.model.Veiculo

class SobreVeiculoDialog : DialogFragment() {

    private lateinit var veiculo: Veiculo
    private lateinit var bd: BancoDadoConfig

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val dialogSobreVeiculo = AlertDialog.Builder(it)
            val viewBinding = SobreVeiculoDialogBinding.inflate(LayoutInflater.from(context))

            bd = BancoDadoConfig.getInstance(it)

            viewBinding.sbTxtNomeVeiculo.text = veiculo.nomeVeiculo
            viewBinding.sbTxtMarca.text = veiculo.marcaVeiculo
            viewBinding.sbTxtCor.text = veiculo.cor
            viewBinding.sbTxtPlaca.text = veiculo.placaVeiculo
            viewBinding.sbTxtMotor.text = veiculo.motor
            viewBinding.sbCombustivel.setSelection(ArrayAdapter.createFromResource(it,
                R.array.combustivel, id).getPosition(veiculo.combustivel))
            viewBinding.sbCombustivel.isEnabled = false
            viewBinding.sbCambio.setSelection(ArrayAdapter.createFromResource(it,
                 R.array.cambio, id).getPosition(veiculo.tipoCambio))
            viewBinding.sbCambio.isEnabled = false
            viewBinding.sbTxtAnoFabricacao.text = veiculo.ano

            viewBinding.btEditarVeiculo.setOnClickListener {

               val veiculoEditado = Veiculo(veiculo.idV,
                   viewBinding.sbTxtNomeVeiculo.text.toString(),
                   viewBinding.sbTxtMarca.text.toString(),
                   viewBinding.sbTxtCor.text.toString(),
                   viewBinding.sbTxtPlaca.text.toString(),
                   viewBinding.sbTxtMotor.text.toString(),
                   viewBinding.sbCombustivel.selectedItem.toString(),
                   viewBinding.sbCambio.selectedItem.toString(),
                   viewBinding.sbTxtAnoFabricacao.text.toString())

                   startActivity(Intent(requireContext(), TelaCadastro::class.java)
                       .putExtra("veiculoEditado", veiculoEditado))

            }
            viewBinding.btFecharVeiculo.setOnClickListener { dialog?.dismiss() }

            dialogSobreVeiculo.setTitle("                        Veículo")
            dialogSobreVeiculo.setView(viewBinding.root)
            dialogSobreVeiculo.create()

        } ?: throw IllegalStateException("Erro ao exibir tela de veículo")

    }

    fun veiculoSelecionado(veiculoSelecionado: Veiculo) {
        this.veiculo = veiculoSelecionado
    }

}








