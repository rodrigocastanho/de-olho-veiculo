package br.com.devnattiva.deolhoveiculo

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import br.com.devnattiva.deolhoveiculo.configuration.Util
import br.com.devnattiva.deolhoveiculo.databinding.FiltrosDialogBinding
import br.com.devnattiva.deolhoveiculo.model.Manutencao
import java.util.*

class FiltroDialog(
    private val callBack: (Manutencao, Dialog) -> Unit
): DialogFragment() {

    private lateinit var binding: FiltrosDialogBinding

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
        binding = FiltrosDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCampo()
        initOnclick()
    }

    private fun initCampo() {
        binding.etKm.addTextChangedListener(Util.mascKmValor(binding.etKm))
    }

    private fun initOnclick() {
        binding.etDataFiltro.setOnClickListener {
            DatePickerFragmentDialog().exibirDataPicker(
                supportFragmentManager = childFragmentManager,
                editText = binding.etDataFiltro
            )
        }

        binding.btFiltrar.setOnClickListener {
            callBack(preparDados(), requireDialog())
        }

        binding.btCancelar.setOnClickListener {
            callBack(Manutencao(), requireDialog())
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