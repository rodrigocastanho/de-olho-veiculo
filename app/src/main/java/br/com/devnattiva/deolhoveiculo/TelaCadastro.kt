package br.com.devnattiva.deolhoveiculo

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import br.com.devnattiva.deolhoveiculo.controller.ControleVeiculo
import br.com.devnattiva.deolhoveiculo.databinding.ActivityTelaCadastroBinding
import br.com.devnattiva.deolhoveiculo.model.Veiculo
import br.com.devnattiva.deolhoveiculo.repository.BancoDadoConfig
import com.google.android.material.navigation.NavigationView


class TelaCadastro : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var viewCadastroBind: ActivityTelaCadastroBinding

    private lateinit var bd: BancoDadoConfig
    private val controleVeiculo = ControleVeiculo()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewCadastroBind = ActivityTelaCadastroBinding.inflate(layoutInflater)

        setContentView(viewCadastroBind.root)
        setSupportActionBar(viewCadastroBind.appBarCadastro.toolbar)

        bd = BancoDadoConfig.getInstance(applicationContext)

        val veiculoEditado = controleVeiculo.veiculoValorEditado(this)

        viewCadastroBind.appBarCadastro.contentCadastro.nomeveiculo.setText(veiculoEditado?.nomeVeiculo)
        viewCadastroBind.appBarCadastro.contentCadastro.marcaveiculo.setText(veiculoEditado?.marcaVeiculo)
        viewCadastroBind.appBarCadastro.contentCadastro.cor.setText(veiculoEditado?.cor)
        viewCadastroBind.appBarCadastro.contentCadastro.placaveiculo.setText(veiculoEditado?.placaVeiculo)
        viewCadastroBind.appBarCadastro.contentCadastro.motorveiculo.setText(veiculoEditado?.motor)
        viewCadastroBind.appBarCadastro.contentCadastro.combescolha.setSelection(ArrayAdapter.createFromResource(this, R.array.combustivel,
            R.layout.activity_tela_cadastro).getPosition(veiculoEditado?.combustivel))
        viewCadastroBind.appBarCadastro.contentCadastro.cambioescolha.setSelection(ArrayAdapter.createFromResource(this, R.array.cambio,
            R.layout.activity_tela_cadastro).getPosition(veiculoEditado?.tipoCambio))
        viewCadastroBind.appBarCadastro.contentCadastro.fabricaoveiculo.setText(veiculoEditado?.ano)


        viewCadastroBind
            .appBarCadastro
            .contentCadastro.btCadastro.setOnClickListener {
                val veiculo = Veiculo(
                    controleVeiculo.statusVeiculoId(veiculoEditado),
                    viewCadastroBind.appBarCadastro.contentCadastro.nomeveiculo.text.toString(),
                    viewCadastroBind.appBarCadastro.contentCadastro.marcaveiculo.text.toString(),
                    viewCadastroBind.appBarCadastro.contentCadastro.cor.text.toString(),
                    viewCadastroBind.appBarCadastro.contentCadastro.placaveiculo.text.toString(),
                    viewCadastroBind.appBarCadastro.contentCadastro.motorveiculo.text.toString(),
                    viewCadastroBind.appBarCadastro.contentCadastro.combescolha.selectedItem.toString(),
                    viewCadastroBind.appBarCadastro.contentCadastro.cambioescolha.selectedItem.toString(),
                    viewCadastroBind.appBarCadastro.contentCadastro.fabricaoveiculo.text.toString())

                controleVeiculo.salvarVeiculo(veiculo, this)

        }

        val toggle = ActionBarDrawerToggle(
            this, viewCadastroBind.drawerLayout, viewCadastroBind.appBarCadastro.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        viewCadastroBind.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        viewCadastroBind.navView.setNavigationItemSelectedListener(this)

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewCadastroBind.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    viewCadastroBind.drawerLayout.closeDrawer(GravityCompat.START)
                }
            }
        })
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.btExcluir -> true
            else -> super.onOptionsItemSelected(item)
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
                startActivity(Intent(this, TelaSobreApp::class.java))
            }
        }

        viewCadastroBind.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
