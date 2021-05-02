package com.appdesenvol.monitoraveiculo

import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_tela_sobre_app.*
import android.os.Bundle

class TelaSobreApp : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_sobre_app)

        pegarTexto.setText("Lista 1: " + intent.getStringExtra("MANUTENCAO")
                                           +"\n\t\t\t\t\t\t\t\t\t\t\t\t" + intent.getStringExtra("CUSTO")
                                           +"\n\t\t\t\t\t\t\t\t\t\t\t\t" + intent.getStringExtra("DATA")
                                           +"\n\t\t\t\t\t\t\t\t\t\t\t\t" + intent.getStringExtra("KM")

                          +"\n\n"+ "Lista 2: " + intent.getStringExtra("MANUTENCAO2")
                                           +"\n\t\t\t\t\t\t\t\t\t\t\t\t" + intent.getStringExtra("CUSTO2")
                                           +"\n\t\t\t\t\t\t\t\t\t\t\t\t" + intent.getStringExtra("DATA2"))

         //pegarTexto.setText(telaStatusManutencao().arraymanu.get(0).tipoManutencao)







    }
}