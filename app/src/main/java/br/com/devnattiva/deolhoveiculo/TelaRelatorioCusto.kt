package br.com.devnattiva.deolhoveiculo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import br.com.devnattiva.deolhoveiculo.configuration.Util
import br.com.devnattiva.deolhoveiculo.controller.ControleRelatorioCusto
import br.com.devnattiva.deolhoveiculo.databinding.ActivityTelaRelatorioCustoBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main


class TelaRelatorioCusto : AppCompatActivity(), AdapterView.OnItemSelectedListener  {

     private lateinit var viewRelatorioCustoBind: ActivityTelaRelatorioCustoBinding
     private val idVeiculos = mutableListOf<Long>()

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewRelatorioCustoBind = ActivityTelaRelatorioCustoBinding.inflate(layoutInflater)
        setContentView(viewRelatorioCustoBind.root)

        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        val contextActivity = this

        val veiculos = CoroutineScope(IO).async {
            return@async ControleRelatorioCusto().buscaVeiculoCusto(contextActivity)
        }

        CoroutineScope(Main).launch {
            idVeiculos.addAll(veiculos.await().keys.toList())
            selecionadorBuscaVeiculo(veiculos.await().values.toList())
        }

        viewRelatorioCustoBind.edDataInicialCusto.setOnClickListener {
            DatePickerFragmentDialog().exibirDataPicker(supportFragmentManager, viewRelatorioCustoBind.edDataInicialCusto)
            viewRelatorioCustoBind.btFiltroCusto?.visibility = View.VISIBLE
        }

        viewRelatorioCustoBind.edDataFinalCusto.setOnClickListener {
            DatePickerFragmentDialog().exibirDataPicker(supportFragmentManager, viewRelatorioCustoBind.edDataFinalCusto)
        }

        viewRelatorioCustoBind.rcCusto.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        viewRelatorioCustoBind.rcCusto.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        viewRelatorioCustoBind.telaRelatorioCusto.setOnClickListener {
            Util.fecharTeclado(this)
        }

    }

    private fun selecionadorBuscaVeiculo(nomeVeiculos: List<String>) {
        viewRelatorioCustoBind.buscaVeiculoCusto.adapter = ArrayAdapter(this, android.R.layout.preference_category, idVeiculos)
        viewRelatorioCustoBind.buscaVeiculoCusto.adapter = ArrayAdapter(this, android.R.layout.preference_category, nomeVeiculos)
        viewRelatorioCustoBind.buscaVeiculoCusto.onItemSelectedListener = this

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        ControleRelatorioCusto().alterarEstadoCamposDataBotao(idVeiculos[position], viewRelatorioCustoBind)
        viewRelatorioCustoBind.btFiltroCusto?.setOnClickListener {
            ControleRelatorioCusto().filtrarCustoManutencao(idVeiculos[position], viewRelatorioCustoBind.edDataInicialCusto,
                viewRelatorioCustoBind.edDataFinalCusto, this, viewRelatorioCustoBind)

        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}