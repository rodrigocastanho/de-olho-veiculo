package br.com.devnattiva.deolhoveiculo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import br.com.devnattiva.deolhoveiculo.databinding.ActivityTelaaberturaBinding

class TelaAbertura : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewActivityAbertura = ActivityTelaaberturaBinding.inflate(layoutInflater)
        setContentView(viewActivityAbertura.root)

        supportActionBar?.hide()

        Handler(mainLooper).postDelayed({
            super.finish()
            startActivity(Intent(this@TelaAbertura, TelaPrincipalmain::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        },6000)

    }

}
