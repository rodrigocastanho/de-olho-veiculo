package br.com.devnattiva.deolhoveiculo

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.ArrayAdapter
import br.com.devnattiva.deolhoveiculo.repository.BancoDadoConfig
import br.com.devnattiva.deolhoveiculo.controller.ControleVeiculo
import br.com.devnattiva.deolhoveiculo.model.Veiculo
import kotlinx.android.synthetic.main.activity_tela_cadastro.*
import kotlinx.android.synthetic.main.app_bar_tela_cadastro.*
import kotlinx.android.synthetic.main.content_tela_cadastro.*

class TelaCadastro : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var bd: BancoDadoConfig
    private val controleVeiculo = ControleVeiculo()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_cadastro)
        setSupportActionBar(toolbar)

        bd = BancoDadoConfig.getInstance(applicationContext)

        val veiculoEditado = controleVeiculo.veiculoValorEditado(this)

        nomeveiculo.setText(veiculoEditado?.nomeVeiculo)
        marcaveiculo.setText(veiculoEditado?.marcaVeiculo)
        placaveiculo.setText(veiculoEditado?.placaVeiculo)
        motorveiculo.setText(veiculoEditado?.motor)
        combescolha.setSelection(ArrayAdapter.createFromResource(this, R.array.combustivel,
            R.layout.activity_tela_cadastro).getPosition(veiculoEditado?.combustivel))
        cambioescolha.setSelection(ArrayAdapter.createFromResource(this, R.array.cambio,
            R.layout.activity_tela_cadastro).getPosition(veiculoEditado?.tipoCambio))
        fabricaoveiculo.setText(veiculoEditado?.ano)


        btCadastro.setOnClickListener {


            val veiculo = Veiculo(
                controleVeiculo.statusVeiculoId(veiculoEditado),
                nomeveiculo.text.toString(),
                marcaveiculo.text.toString(),
                placaveiculo.text.toString(),
                motorveiculo.text.toString(),
                combescolha.selectedItem.toString(),
                cambioescolha.selectedItem.toString(),
                fabricaoveiculo.text.toString())

            controleVeiculo.salvarVeiculo(veiculo, this)

        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

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
                startActivity(Intent(this, TelaPrincipalmain::class.java))
            }
            R.id.StatusManuteção -> {
                //Chamar a tela de Status de manutenção
                startActivity(Intent(this, TelaStatusManutencao::class.java))
            }
            R.id.RelatorioCusto -> {
                startActivity(Intent(this, TelaRelatorioCusto::class.java))
            }
            R.id.backup_restaura -> {
                startActivity(Intent(this, TelaBackup::class.java))
            }
            R.id.SobreApp -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
