package br.com.devnattiva.deolhoveiculo


import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.devnattiva.deolhoveiculo.configuration.Util
import br.com.devnattiva.deolhoveiculo.controller.ControleManutencao
import br.com.devnattiva.deolhoveiculo.controller.ControleVeiculo
import br.com.devnattiva.deolhoveiculo.controller.StatusManutencaoAdapterRW
import br.com.devnattiva.deolhoveiculo.databinding.ActivityTelaStatusManutencaoBinding
import com.google.android.material.navigation.NavigationView


class TelaStatusManutencao : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var viewActivity: ActivityTelaStatusManutencaoBinding

    private val veiculos = ControleVeiculo()
    private var veiculoId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewActivity = ActivityTelaStatusManutencaoBinding.inflate(layoutInflater)
        setContentView(viewActivity.root)
        setSupportActionBar(viewActivity.appBarManutencao.toolbar)

        val controleManutencoes = ControleManutencao()
        val veiculosBusca = veiculos.buscarVeiculo(this)

        val buscarVeiculo = viewActivity.appBarManutencao.contentManutencao.buscaVeiculo

        buscarVeiculo.adapter = ArrayAdapter(this, android.R.layout.preference_category, veiculosBusca.first)
        buscarVeiculo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                veiculoId = veiculosBusca.second[position]

                Util.pegarVeiculoId(veiculoId)

                controleManutencoes.fluxoManutencao(veiculoId, this@TelaStatusManutencao, supportFragmentManager, viewActivity.appBarManutencao.contentManutencao)

                Log.i("MEU ID", " ID $veiculoId")

            }

        }

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        viewActivity.appBarManutencao.contentManutencao.recyclerViewManutencao.layoutManager = layoutManager
        viewActivity.appBarManutencao.contentManutencao.recyclerViewManutencao.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        viewActivity.appBarManutencao.contentManutencao.recyclerViewManutencao.adapter = StatusManutencaoAdapterRW(controleManutencoes.arrayManutencoes,this, supportFragmentManager)


        viewActivity.appBarManutencao.contentManutencao.btFtAddManutencao.setOnClickListener {
            controleManutencoes.arrayManutencoes.add(controleManutencoes.addManutencao())
            viewActivity.appBarManutencao.contentManutencao.recyclerViewManutencao.adapter?.notifyItemInserted(controleManutencoes.arrayManutencoes.size)
            layoutManager.scrollToPosition(controleManutencoes.arrayManutencoes.size-1)

        }

        viewActivity.appBarManutencao.contentManutencao.statusManutencao.setOnClickListener{
            Util.fecharTeclado(this)
        }

        val toggle = ActionBarDrawerToggle(
            this, this.viewActivity.drawerLayout, viewActivity.appBarManutencao.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        this.viewActivity.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        this.viewActivity.navView.setNavigationItemSelectedListener(this)

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
            }
        })
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.tela_status_manutencao, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        veiculos.excluirVeiculo(veiculoId, this)

        return when (item.itemId) {
            R.id.btExcluir -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.Home -> {
                //Chamar a tela de principal
                startActivity(Intent(this, TelaPrincipalmain::class.java))
            }
            R.id.CadastroVeiculo -> {
                //Chamar a tela de cadastro
                startActivity(Intent(this, TelaCadastro::class.java))
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

        viewActivity.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
