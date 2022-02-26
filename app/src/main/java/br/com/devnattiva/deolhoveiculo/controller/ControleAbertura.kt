package br.com.devnattiva.deolhoveiculo.controller

import android.app.Activity
import android.util.Log
import br.com.devnattiva.deolhoveiculo.configuration.Util
import br.com.devnattiva.deolhoveiculo.repository.BancoDadoConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.lang.Exception

class ControleAbertura(val context: Activity) {

    private lateinit var bd: BancoDadoConfig

    fun possuiVeiculoCadastrado() {
        bd = BancoDadoConfig.getInstance(context.applicationContext)
        CoroutineScope(IO).launch {
            try {
                  val veiculos = bd.controleDAO().buscaVeiculoAcesso()
                  if (!veiculos.isNullOrEmpty()) {
                      Util.acessoInicial(context, true)
                  } else Util.acessoInicial(context, false)

              } catch (e: Exception) {
                  Log.e("ERRO_VEICULO", "BUSCA_ACESSO: $e")
              } finally {
                  bd.close()
              }
        }
    }
}