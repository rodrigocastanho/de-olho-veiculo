package br.com.devnattiva.deolhoveiculo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class TelaSobreApp : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_sobre_app)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
}