package br.com.devnattiva.deolhoveiculo

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class TelaRelatorioCusto : AppCompatActivity() {
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_relatorio_custo)

        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)

    }
}