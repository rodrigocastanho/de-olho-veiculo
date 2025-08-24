package br.com.devnattiva.deolhoveiculo

import android.app.Activity
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.FragmentManager
import br.com.devnattiva.deolhoveiculo.configuration.Util
import br.com.devnattiva.deolhoveiculo.databinding.AddManutencaoDialogBinding
import br.com.devnattiva.deolhoveiculo.model.Manutencao
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AddManutencaoDialog(
    private val context: Activity,
    private var manutencao: Manutencao ?= null,
    private val fragmentManager: FragmentManager,
) {
    private lateinit var binding: AddManutencaoDialogBinding
    private var titulo = "Nova Manutenção"
    private var nomeBotao = "Adicionar"
    fun createDialog(
        primaryButtonAction: (manutencao: Manutencao, dialog: DialogInterface) -> Unit,
        secundaryButtonAction: (dialog: DialogInterface) -> Unit
    ) {
        binding = AddManutencaoDialogBinding.inflate(LayoutInflater.from(context))
        initOnclick()
        initCampos()

        val dialogBuilder = MaterialAlertDialogBuilder(context,R.style.ThemeCustomDialog)
            .setView(binding.root)
            .setBackground(android.R.color.transparent.toDrawable())
            .setTitle(titulo)
            .setPositiveButton(nomeBotao, null)
            .setNegativeButton("Cancelar") { dialog, _ ->
                secundaryButtonAction.invoke(dialog)
            }
            .setCancelable(false)
            .create()

        dialogBuilder.show()
        dialogBuilder.getButton(DialogInterface.BUTTON_POSITIVE)
            .setOnClickListener {
                val valor = preparDados()
                if (valor.validacao().second) {
                    primaryButtonAction.invoke(preparDados(), dialogBuilder)
                } else {
                    binding.tiKmAtual.error = valor.validacao().first.getValue(0)
                    binding.tiKmFinal.error = valor.validacao().first.getValue(1)
                    binding.tiData.error = valor.validacao().first.getValue(2)
                    binding.tiCusto.error = valor.validacao().first.getValue(3)
                    binding.tiTipoManutencao.error = valor.validacao().first.getValue(4)
                }
            }

        dialogBuilder.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun initOnclick() {
        binding.etData.setOnClickListener {
            DatePickerFragmentDialog().exibirDataPicker(
                supportFragmentManager = fragmentManager,
                editText = binding.etData
            )
        }
    }

    private fun initCampos() {
        binding.etKmAtual.addTextChangedListener(Util.mascKmValor(binding.etKmAtual))
        binding.etKmFinal.addTextChangedListener(Util.mascKmValor(binding.etKmFinal))
        binding.etCusto.addTextChangedListener(Util.mascMonetario(binding.etCusto))

        Util.limparErroCampo(binding.etTipoManutencao, binding.tiTipoManutencao)
        Util.limparErroCampo(binding.etKmAtual, binding.tiKmAtual)
        Util.limparErroCampo(binding.etKmFinal, binding.tiKmFinal)
        Util.limparErroCampo(binding.etData, binding.tiData)
        Util.limparErroCampo(binding.etCusto, binding.tiCusto)

        manutencao?.let {
            titulo = "Alterar manutenção"
            binding.etTipoManutencao.setText(it.tipoManutencao)
            binding.etKmAtual.setText(it.kmtrocaAtual)
            binding.etKmFinal.setText(it.kmtroca)
            binding.etData.setText(Util.converteDataTexto(it.data))
            binding.etCusto.setText(it.custo)
            binding.etObservacao.setText(it.observacao)
            nomeBotao = "Alterar"
        }
    }

    private fun preparDados(): Manutencao {
        val dado = Manutencao()
        dado.idM = manutencao?.idM ?: 0L
        dado.idVM = manutencao?.idVM ?: 0L
        dado.tipoManutencao = binding.etTipoManutencao.text.toString()
        dado.kmtrocaAtual = binding.etKmAtual.text.toString()
        dado.kmtroca = binding.etKmFinal.text.toString()
        dado.data = Util.converteTextoData(binding.etData.text.toString())
        dado.custo = binding.etCusto.text.toString()
        dado.observacao = binding.etObservacao.text.toString()
        return dado
    }
}