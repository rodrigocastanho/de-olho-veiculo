package br.com.devnattiva.deolhoveiculo

import android.content.Intent
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import br.com.devnattiva.deolhoveiculo.databinding.ActivityTelaPrincipalmainBinding


class TelaPrincipalmain : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var viewPrincipalBind: ActivityTelaPrincipalmainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewPrincipalBind = ActivityTelaPrincipalmainBinding.inflate(layoutInflater)
        val toolbarBind = viewPrincipalBind.appBarTelaPrincipal.toolbar
        val viewConteudoBind = viewPrincipalBind.appBarTelaPrincipal.contentTelaPrincipal

        setContentView(viewPrincipalBind.root)
        setSupportActionBar(toolbarBind)

        viewConteudoBind.ImgCarro.setOnClickListener {
            //Parte que ira chamar a telaCadastro
            //Toast.makeText(this@telaPrincipalmain, "Teste do Click.", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, TelaCadastro::class.java))

        }

        val toggle = ActionBarDrawerToggle(
            this, viewPrincipalBind.drawerLayout, toolbarBind,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        viewPrincipalBind.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

       viewPrincipalBind.navView.setNavigationItemSelectedListener(this)

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewPrincipalBind.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    viewPrincipalBind.drawerLayout.closeDrawer(GravityCompat.START)
                }
            }
        })
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.Home -> {
                //Chama tela Principal
                startActivity(Intent(this, TelaPrincipalmain::class.java))
            }
            R.id.CadastroVeiculo -> {
                //Chamar a tela de cadastro
                startActivity(Intent(this, TelaCadastro::class.java))
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

        viewPrincipalBind.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

}

