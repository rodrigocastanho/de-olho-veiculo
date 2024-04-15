package br.com.devnattiva.deolhoveiculo

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import br.com.devnattiva.deolhoveiculo.configuration.Util
import br.com.devnattiva.deolhoveiculo.databinding.AddManutencaoDialogBinding
import br.com.devnattiva.deolhoveiculo.model.Manutencao

class AddManutencaoDialog(
    private var manutencao: Manutencao ?= null,
    private val callBack: (Manutencao, Dialog) -> Unit
): DialogFragment() {

    private lateinit var binding: AddManutencaoDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddManutencaoDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCampos()
        initOnclick()

        manutencao?.let {
            binding.tvNovaManutencao.text = "Alterar manutenção"
            binding.etTipoManutencao.setText(it.tipoManutencao)
            binding.etKmAtual.setText(it.kmtrocaAtual)
            binding.etKmFinal.setText(it.kmtroca)
            binding.etData.setText(Util.converteDataTexto(it.data))
            binding.etCusto.setText(it.custo)
            binding.etObservacao.setText(it.observacao)
            binding.btSalvar.text = "Alterar"
        }
    }

    private fun initOnclick() {
        binding.etData.setOnClickListener {
            DatePickerFragmentDialog().exibirDataPicker(
                supportFragmentManager = childFragmentManager,
                editText = binding.etData
            )
        }

        binding.btSalvar.setOnClickListener {
            inserirDados()
        }

        binding.btCancelar.setOnClickListener {
            dismiss()
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
    }

    private fun inserirDados() {
        val valor = preparDados()
        if (valor.validacao().second) {
            callBack(valor, requireDialog())
        } else {
            binding.tiKmAtual.error = valor.validacao().first.getValue(0)
            binding.tiKmFinal.error = valor.validacao().first.getValue(1)
            binding.tiData.error = valor.validacao().first.getValue(2)
            binding.tiCusto.error = valor.validacao().first.getValue(3)
            binding.tiTipoManutencao.error = valor.validacao().first.getValue(4)
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