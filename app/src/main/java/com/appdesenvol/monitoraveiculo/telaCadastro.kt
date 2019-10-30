package com.appdesenvol.monitoraveiculo

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.appdesenvol.monitoraveiculo.bancodados.BancoDadoConfig
import com.appdesenvol.monitoraveiculo.objetos.VeiculoManutencao
import com.appdesenvol.monitoraveiculo.objetos.Veiculo
import kotlinx.android.synthetic.main.activity_tela_cadastro.*
import kotlinx.android.synthetic.main.app_bar_tela_cadastro.*
import kotlinx.android.synthetic.main.content_tela_cadastro.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.sql.SQLException

class telaCadastro : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var bd: BancoDadoConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_cadastro)
        setSupportActionBar(toolbar)


        var nomeVeiculo = nomeveiculo.text
        var marcaVeiculo = marcaveiculo.text
        var placaVeiculo = placaveiculo.text
        var motorVeiculo = motorveiculo.text
        var tipoCombustivel = combescolha
        var tipoCambio = cambioescolha
        var anoFabricacao = fabricaoveiculo.text

        bd = BancoDadoConfig.getInstance(applicationContext)


        btCadastro.setOnClickListener {

            if (nomeVeiculo.isNotEmpty()) {
                Log.i("TESTE EMP", "CADASTRO OK")

                var veiculo = Veiculo(
                    0,
                    nomeVeiculo.toString(),
                    marcaVeiculo.toString(),
                    placaVeiculo.toString(),
                    motorVeiculo.toString(),
                    tipoCombustivel.selectedItem.toString(),
                    tipoCambio.selectedItem.toString(),
                    anoFabricacao.toString()
                )



                Toast.makeText(getApplicationContext(), "VEICULO CADASTRADO", Toast.LENGTH_SHORT).show()

            CoroutineScope(IO).launch {

                    try {

                        bd.controleDAO().salvarDados(veiculo)

                    } catch (e: SQLException) {

                        Log.e("ERRO Veiculo insert", " VERIFICAR ERRO: " + e)

                    }

                }


                Log.i(
                    "-VEICULO-",
                    "id " + veiculo.idV + " nome " + veiculo.nomeVeiculo + " marca " + veiculo.marcaVeiculo + " Combustivel " + veiculo.combustivel + " " +
                            " Placa " + veiculo.placaVeiculo + " Motor " + veiculo.motor + " Cambio " + veiculo.tipoCambio + " Ano " + veiculo.ano
                )

            } else {


                Log.w("-AVISO-", "FALTOU NOME DO VEICULO")
                Toast.makeText(getApplicationContext(), "FALTOU NOME DO VEICULO", Toast.LENGTH_SHORT).show()

                //consultateste()
            }

        }


        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

   /* fun consultateste() {

        CoroutineScope(IO).launch {

            val buscveiculo = bd.controleDAO().buscaVeiculoManutencao()
            for (vm: VeiculoManutencao in buscveiculo) {

                Log.i(
                    "DADOS DO BANCO", "id " + vm.veiculo.idV +
                            " nome " + vm.veiculo.nomeVeiculo + " marca " + vm.veiculo.marcaVeiculo + " Combustivel " + vm.veiculo.combustivel + " " +
                            " Placa " + vm.veiculo.placaVeiculo + " Motor " + vm.veiculo.motor + " Cambio " + vm.veiculo.tipoCambio + " Ano " + vm.veiculo.ano +

                            "---DADOS BANCO MANUTENCAO---" + " id " + vm.manutencao.idM + " id V_M " + vm.veiculo.nomeVeiculo + " Manutencao " + vm.manutencao.tipoManutencao +
                            " KM Troca " + vm.manutencao.kmtroca + " Data " + vm.manutencao.data + " Custo " + vm.manutencao.custo
                )


            }
        }

    }*/

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.btExcluir -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.Home -> {
                //Chama tela Principal
                startActivity(Intent(this@telaCadastro, telaPrincipalmain::class.java))
            }
            R.id.CadastroVeiculo -> {

            }
            R.id.StatusManuteção -> {
                //Chamar a tela de Status de manutenção
                startActivity(Intent(this@telaCadastro, telaStatusManutencao::class.java))


            }
            R.id.RelatorioCusto -> {

            }
            R.id.SobreApp -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
