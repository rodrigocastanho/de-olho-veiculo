package br.com.devnattiva.deolhoveiculo


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import br.com.devnattiva.deolhoveiculo.configuration.Util
import br.com.devnattiva.deolhoveiculo.controller.ControleManutencao
import br.com.devnattiva.deolhoveiculo.controller.ControleVeiculo
import br.com.devnattiva.deolhoveiculo.controller.StatusManutencaoAdapterRW
import br.com.devnattiva.deolhoveiculo.databinding.ActivityTelaStatusManutencaoBinding
import br.com.devnattiva.deolhoveiculo.model.Manutencao
import com.google.android.material.navigation.NavigationView.*


class TelaStatusManutencao : AppCompatActivity(),
    OnNavigationItemSelectedListener {

    private lateinit var viewActivity: ActivityTelaStatusManutencaoBinding

    private val veiculos = ControleVeiculo()
    private var veiculoId: Long = 0
    private lateinit var adapterManutencao: StatusManutencaoAdapterRW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewActivity = ActivityTelaStatusManutencaoBinding.inflate(layoutInflater)
        setContentView(viewActivity.root)
        setSupportActionBar(viewActivity.appBarManutencao.toolbar)

        val controleManutencoes = ControleManutencao(this)
        val veiculosBusca = veiculos.buscarVeiculo(this)

        val buscarVeiculo = viewActivity.appBarManutencao.contentManutencao.buscaVeiculo

        buscarVeiculo.adapter = ArrayAdapter(this, android.R.layout.preference_category, veiculosBusca.first)
        buscarVeiculo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                veiculoId = veiculosBusca.second[position]
                controleManutencoes.fluxoManutencao(
                    veiculoId,
                    this@TelaStatusManutencao,
                    supportFragmentManager,
                    viewActivity.appBarManutencao.contentManutencao,
                    callBack = { adapterManutencao.submitList(controleManutencoes.manutencoes) }
                )
                Log.i("MEU ID", " ID $veiculoId")
            }
        }

        viewActivity.appBarManutencao.contentManutencao
            .ivFiltro.setOnClickListener {
                FiltroDialog(this, supportFragmentManager)
                    .createDialog(
                        primaryButtonAction = { manutencao, dialog ->
                            dialog.dismiss()
                            val manutencoesFiltro = controleManutencoes.filtrarManutencoes(manutencao)
                            adapterManutencao.submitList(manutencoesFiltro)
                        },
                        secundaryButtonAction = { manutencao, dialog ->
                            dialog.dismiss()
                            val manutencoesFiltro = controleManutencoes.filtrarManutencoes(manutencao)
                            adapterManutencao.submitList(manutencoesFiltro)
                        }
                )
        }

        adapterManutencao = StatusManutencaoAdapterRW(
            context = this@TelaStatusManutencao,
            fragment = supportFragmentManager,
            controleManutencao = controleManutencoes
        )

        viewActivity.appBarManutencao.contentManutencao
            .recyclerViewManutencao.apply {
                setHasFixedSize(true)
                adapter = adapterManutencao
            }

        viewActivity.appBarManutencao
            .contentManutencao.ivAddManutencao.setOnClickListener {
                AddManutencaoDialog(
                    context = this,
                    fragmentManager = supportFragmentManager
                ).createDialog(
                    primaryButtonAction = { manutencao, dialog ->
                        dialog.dismiss()
                        viewActivity.appBarManutencao.contentManutencao.tvNaoManutencao.isVisible = false
                        viewActivity.appBarManutencao.contentManutencao.ivFiltro.isInvisible = false
                        viewActivity.appBarManutencao.contentManutencao.recyclerViewManutencao.isInvisible = false
                        manutencao.idVM = veiculoId
                        controleManutencoes.salvarManutencao(manutencao, this)
                        controleManutencoes.adicionarManutencao(manutencao)
                        adapterManutencao.submitList(controleManutencoes.manutencoes)
                    },
                    secundaryButtonAction = { dialog -> dialog.dismiss()}
                )
        }

        viewActivity.appBarManutencao.contentManutencao.statusManutencao.setOnClickListener {
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
                } else finish()
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