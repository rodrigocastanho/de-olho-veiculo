package br.com.devnattiva.deolhoveiculo

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import br.com.devnattiva.deolhoveiculo.databinding.SobreVeiculoDialogBinding
import br.com.devnattiva.deolhoveiculo.model.Veiculo
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SobreVeiculoDialog(
    private val context: Context,
    private val veiculo: Veiculo
) {
    private lateinit var binding: SobreVeiculoDialogBinding

    fun createDialog(
        primaryButtonAction: (dialog: DialogInterface) -> Unit,
        secundaryButtonAction: (dialog: DialogInterface) -> Unit
    ) {
        binding = SobreVeiculoDialogBinding.inflate(LayoutInflater.from(context))

        binding.tvNomeVeiculo.text = veiculo.nomeVeiculo
        binding.tvMarca.text = veiculo.marcaVeiculo
        binding.tvCor.text = veiculo.cor
        binding.tvPlaca.text = veiculo.placaVeiculo
        binding.tvMotor.text = veiculo.motor
        binding.tvCombustivel.text = veiculo.combustivel
        binding.tvCambio.text = veiculo.tipoCambio
        binding.tvAnoFabricacao.text = veiculo.ano

       val dialogBuilder = MaterialAlertDialogBuilder(context, R.style.ThemeCustomDialog)
           .setView(binding.root)
           .setBackground(android.R.color.transparent.toDrawable())
           .setTitle("Veículo")
           .setPositiveButton("Editar") { dialog, _ ->
               primaryButtonAction.invoke(dialog)
           }
           .setNegativeButton("Cancelar") { dialog, _ ->
                   secundaryButtonAction.invoke(dialog)
           }
           .setCancelable(false)
           .create()

        dialogBuilder.show()
        dialogBuilder.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}