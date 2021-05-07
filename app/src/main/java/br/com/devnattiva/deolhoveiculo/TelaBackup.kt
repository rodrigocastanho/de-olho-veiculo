package br.com.devnattiva.deolhoveiculo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import br.com.devnattiva.deolhoveiculo.controller.ControleBackupRestaurar
import kotlinx.android.synthetic.main.activity_tela_backup.*

@RequiresApi(Build.VERSION_CODES.O)
class TelaBackup : AppCompatActivity() {

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_backup)

        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)

       btn_fazer_backup.setOnClickListener {
           ControleBackupRestaurar().backupDados(this)
       }

       btn_restaurar_backup.setOnClickListener {
           ControleBackupRestaurar().restaurarBackupDados(this)
       }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
           ControleBackupRestaurar().arquivoSelecionadoRestaurar(this, requestCode, resultCode, resultData)

    }

}