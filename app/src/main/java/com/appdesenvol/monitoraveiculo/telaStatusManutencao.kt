package com.appdesenvol.monitoraveiculo


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.Constraints
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.support.v4.widget.DrawerLayout
import android.support.design.widget.NavigationView
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.appdesenvol.monitoraveiculo.bancodados.BancoDadoConfig
import com.appdesenvol.monitoraveiculo.logica.AdapterRecyclerView
import com.appdesenvol.monitoraveiculo.logica.ControleManutencao
import com.appdesenvol.monitoraveiculo.logica.ControleVeiculo
import com.appdesenvol.monitoraveiculo.logica.Util
import com.appdesenvol.monitoraveiculo.model.Veiculo
import kotlinx.android.synthetic.main.activity_tela_principalmain.*
import kotlinx.android.synthetic.main.content_tela_cadastro.*
import kotlinx.android.synthetic.main.content_tela_status_manutencao.*
import kotlinx.android.synthetic.main.sobre_veiculo_dialog.*
import kotlinx.android.synthetic.main.tipo_manutencao.*
import kotlinx.android.synthetic.main.tipo_manutencao.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


class telaStatusManutencao : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val controleManutencoes = ControleManutencao()
    private lateinit var bd: BancoDadoConfig
    private var veiculoId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_status_manutencao)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val contextActivy = this
        bd = BancoDadoConfig.getInstance(applicationContext)

        val buscarVeiculo = buscaVeiculo
        val listId = mutableListOf<Long>(0)
        val listVeiculo = mutableListOf<String>("\t\t\t\t\t\t Buscar Veiculo")


        buscarVeiculo.adapter = ArrayAdapter<Long>(this, android.R.layout.preference_category, listId)
        buscarVeiculo.adapter = ArrayAdapter<String>(this, android.R.layout.preference_category, listVeiculo)

        buscarVeiculo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                veiculoId = listId[position]

                Util.pegarVeiculoId(veiculoId)

                controleManutencoes.fluxoManutencao(veiculoId, contextActivy, supportFragmentManager)

                Log.i("MEU ID", " ID " + veiculoId)

            }

        }

        CoroutineScope(IO).launch {
            try {

                val buscveiculo = bd.controleDAO().buscaVeiculo()
                for (v: Veiculo in buscveiculo) {

                    listVeiculo.add(" \t\t\t\t\t\t " + v.nomeVeiculo)
                    listId.add(v.idV)
                }

            } catch (e: Exception) {
                Log.e("ERRO_BUSCA_VM", " ERRO_BUSCA_VEICULO_MANUTENÇÂO " + e)

            }
        }

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewManutencao.layoutManager = layoutManager
        recyclerViewManutencao.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerViewManutencao.adapter = AdapterRecyclerView(controleManutencoes.arrayManutencoes,this, supportFragmentManager)


        bt_ft_add_manutencao.setOnClickListener {
            controleManutencoes.arrayManutencoes.add(controleManutencoes.addManutencao())
            recyclerViewManutencao.adapter?.notifyItemInserted(controleManutencoes.arrayManutencoes.size)
            layoutManager.scrollToPosition(controleManutencoes.arrayManutencoes.size-1)

        }

        statusManutencao.setOnClickListener{
            Util.fecharTeclado(this)
        }


        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
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

        ControleVeiculo().excluirVeiculo(veiculoId, this)

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
                startActivity(Intent(this, telaPrincipalmain::class.java))
            }
            R.id.CadastroVeiculo -> {
                //Chamar a tela de cadastro
                startActivity(Intent(this, telaCadastro::class.java))
            }
            R.id.StatusManuteção -> {

            }
            R.id.RelatorioCusto -> {

            }
            R.id.backup_restaura -> {
                startActivity(Intent(this, TelaBackup::class.java))
            }
            R.id.SobreApp -> {
                startActivity(Intent(this, telaSobreApp::class.java))


            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
