package com.appdesenvol.monitoraveiculo

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_tela_principalmain.*
import kotlinx.android.synthetic.main.app_bar_tela_principalmain.*

class telaPrincipalmain : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_principalmain)
        setSupportActionBar(toolbar)

        val imgcarro = findViewById(R.id.ImgCarro) as ImageView

        imgcarro.setOnClickListener{

            //Parte que ira chamar a telaCadastro
            //Toast.makeText(this@telaPrincipalmain, "Teste do Click.", Toast.LENGTH_LONG).show()
            startActivity(Intent(this@telaPrincipalmain, telaCadastro::class.java))

        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.tela_principalmain, menu)
        return true
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
            R.id.CadastroVeiculo -> {
                //Chamar a tela de cadastro
                startActivity(Intent(this@telaPrincipalmain, telaCadastro::class.java))
            }
            R.id.StatusManuteção -> {
                //Chamar a tela de Status de manutenção
                startActivity(Intent(this@telaPrincipalmain, telaStatusManutencao::class.java))


            }
            R.id.RelatorioCusto -> {

            }
            R.id.SobreApp -> {

                startActivity(Intent(this@telaPrincipalmain, telaSobreApp::class.java))


            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
