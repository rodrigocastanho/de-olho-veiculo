package br.com.devnattiva.deolhoveiculo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import br.com.devnattiva.deolhoveiculo.controller.ControleBackupRestaurar
import br.com.devnattiva.deolhoveiculo.databinding.ActivityTelaBackupBinding

class TelaBackup : AppCompatActivity() {

    private lateinit var viewActivityBackup: ActivityTelaBackupBinding

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewActivityBackup = ActivityTelaBackupBinding.inflate(layoutInflater)
        setContentView(viewActivityBackup.root)

        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)

        val arquivo = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            ControleBackupRestaurar().arquivoSelecionadoRestaurar(this, result.resultCode, result.data, viewActivityBackup)
        }

        viewActivityBackup.btnFazerBackup.setOnClickListener {
            ControleBackupRestaurar().backupDados(this, viewActivityBackup)
        }

        viewActivityBackup.btnRestaurarBackup.setOnClickListener {
             ControleBackupRestaurar().restaurarBackupDados(this, arquivo)
        }

    }
}


