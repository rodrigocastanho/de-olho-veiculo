package br.com.devnattiva.deolhoveiculo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import android.view.Window
import br.com.devnattiva.deolhoveiculo.databinding.ActivityTelaaberturaBinding

class TelaAbertura : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewActivityAbertura = ActivityTelaaberturaBinding.inflate(layoutInflater)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(android.R.attr.windowFullscreen, android.R.attr.windowFullscreen)
        setContentView(viewActivityAbertura.root)

        Handler(mainLooper).postDelayed({
            startActivity(Intent(this@TelaAbertura, TelaPrincipalmain::class.java))
            finish()
        },6000)

    }
}
