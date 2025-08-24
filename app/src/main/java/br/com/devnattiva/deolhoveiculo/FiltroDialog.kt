package br.com.devnattiva.deolhoveiculo

import android.app.Activity
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.FragmentManager
import br.com.devnattiva.deolhoveiculo.configuration.Util
import br.com.devnattiva.deolhoveiculo.databinding.FiltrosDialogBinding
import br.com.devnattiva.deolhoveiculo.model.Manutencao
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class FiltroDialog(
    private val context: Activity,
    val fragmentManager: FragmentManager,
) {
    private lateinit var binding: FiltrosDialogBinding

    fun createDialog(
        primaryButtonAction: (manutencao: Manutencao, dialog: DialogInterface) -> Unit,
        secundaryButtonAction: (manutencao: Manutencao, dialog: DialogInterface) -> Unit
    ) {
        binding = FiltrosDialogBinding.inflate(LayoutInflater.from(context))

        initCampo()
        initOnclick()

        val dialogBuilder = MaterialAlertDialogBuilder(context, R.style.ThemeCustomDialog)
            .setView(binding.root)
            .setBackground(android.R.color.transparent.toDrawable())
            .setTitle("Filtro")
            .setPositiveButton("Filtrar") { dialog, _ ->
                primaryButtonAction.invoke(preparDados(), dialog)
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                secundaryButtonAction.invoke(Manutencao(), dialog)
            }
            .setCancelable(false)
            .create()

        dialogBuilder.show()
        dialogBuilder.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun initCampo() {
        binding.etKm.addTextChangedListener(Util.mascKmValor(binding.etKm))
    }

    private fun initOnclick() {
        binding.etDataFiltro.setOnClickListener {
            DatePickerFragmentDialog().exibirDataPicker(
                supportFragmentManager = fragmentManager,
                editText = binding.etDataFiltro
            )
        }
    }

    private fun preparDados(): Manutencao {
        val dado = Manutencao()
        dado.tipoManutencao = binding.etNomeManutencao.text.toString()
        dado.kmtroca = binding.etKm.text.toString()
        dado.kmtrocaAtual = binding.etKm.text.toString()
        dado.data = Util.converteTextoData(binding.etDataFiltro.text.toString())
        return dado
    }
}