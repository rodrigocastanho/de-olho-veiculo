package br.com.devnattiva.deolhoveiculo.controller


import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import br.com.devnattiva.deolhoveiculo.SobreVeiculoDialog
import br.com.devnattiva.deolhoveiculo.databinding.ContentTelaStatusManutencaoBinding
import br.com.devnattiva.deolhoveiculo.model.Manutencao
import br.com.devnattiva.deolhoveiculo.model.Veiculo
import br.com.devnattiva.deolhoveiculo.model.VeiculoManutencao
import br.com.devnattiva.deolhoveiculo.repository.BancoDadoConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.SQLException
import java.util.*

class ControleManutencao {

    private var _manutencoes = mutableListOf<Manutencao>()
    val manutencoes get() = _manutencoes.toList()
    private lateinit var bd: BancoDadoConfig

    private fun manutencoesVeiculo(
        veiculoManutecoes: List<VeiculoManutencao>
    ) {
        veiculoManutecoes.map {
            it.manutencao
        }.also { m ->
            _manutencoes.clear()
            _manutencoes = m.toMutableList()
        }
    }

    fun filtrarManutencoes(manutencao: Manutencao): List<Manutencao> {
        val listaAlterada = _manutencoes.filter { predicateFiltro(it, manutencao) }
        return listaAlterada.ifEmpty { _manutencoes }
    }

    private fun predicateFiltro(manutencaoFiltro: Manutencao, manutencao: Manutencao): Boolean {
        return if (manutencao.tipoManutencao.isNotBlank()) {
            manutencaoFiltro.tipoManutencao.contains(manutencao.tipoManutencao)
        } else if (manutencao.kmtrocaAtual.isNotBlank()) {
            manutencaoFiltro.kmtrocaAtual.contains(manutencao.kmtrocaAtual)
        } else if (manutencao.data != null) {
            manutencaoFiltro.data?.time == manutencao.data?.time
        } else if (manutencao.tipoManutencao.isNotBlank() &&
            manutencao.kmtrocaAtual.isNotBlank() &&
            manutencao.data != null) {
            manutencaoFiltro.tipoManutencao.contains(manutencao.tipoManutencao)
                    && manutencaoFiltro.kmtrocaAtual.contains(manutencao.kmtrocaAtual)
                    && manutencaoFiltro.data?.time == manutencao.data?.time
        } else false
    }

    fun adicionarManutencao(manutencao: Manutencao) {
        _manutencoes.add(manutencao)
    }

    fun atualizarManutencao(manutencao: Manutencao, posicao: Int) {
        _manutencoes[posicao] = manutencao
    }

    fun deletarManutencao(manutencao: Manutencao) {
        _manutencoes.remove(manutencao)
    }

    fun fluxoManutencao(
        veiculoId: Long,
        context: Activity,
        supportFragmentManager: FragmentManager,
        viewConteudo: ContentTelaStatusManutencaoBinding,
        callBack: () -> Unit
    ) {
        lateinit var veiculo : Veiculo
        bd = BancoDadoConfig.getInstance(context.applicationContext)

        if(veiculoId != 0L) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    veiculo = bd.controleDAO().buscaVeiculoId(veiculoId)
                    val buscveiculomanutencao = bd.controleDAO().buscaVeiculoManutencao(veiculoId)
                    withContext(Dispatchers.Main) {
                        if (buscveiculomanutencao.isEmpty()) {
                            _manutencoes.clear()
                            viewConteudo.recyclerViewManutencao.isInvisible = true
                            viewConteudo.tvNaoManutencao.isVisible = true
                            viewConteudo.ivFiltro.isInvisible = true
                        } else {
                            viewConteudo.recyclerViewManutencao.isInvisible = false
                            viewConteudo.tvNaoManutencao.isVisible = false
                            viewConteudo.ivFiltro.isInvisible = false
                            manutencoesVeiculo(buscveiculomanutencao)
                            callBack.invoke()
                        }
                    }
                } catch (e: Exception) {
                    Log.e("ERRO_VEICULO_M", "ERRO_VEICULO_MANUTENÇÂO $e")
                } finally {
                    bd.close()
                }
            }

            viewConteudo.ivEditarVeiculo.isEnabled = true
            viewConteudo.ivEditarVeiculo.setOnClickListener {
                val sobreVeiculoDialog = SobreVeiculoDialog()
                sobreVeiculoDialog.veiculoSelecionado(veiculo)
                sobreVeiculoDialog.show(supportFragmentManager, "dialogSobreVeiculo")
            }
            if(viewConteudo.recyclerViewManutencao.visibility == View.INVISIBLE) {
                viewConteudo.txInicioManutencao.visibility = View.GONE
                viewConteudo.tvNaoManutencao.isVisible = false
                viewConteudo.recyclerViewManutencao.visibility = View.INVISIBLE
                viewConteudo.btFtAddManutencao.show()
            }
        } else {
            viewConteudo.ivEditarVeiculo.isEnabled = false
            viewConteudo.ivFiltro.isInvisible = true
            viewConteudo.tvNaoManutencao.isVisible = false
            viewConteudo.txInicioManutencao.visibility = View.VISIBLE
            viewConteudo.recyclerViewManutencao.visibility = View.INVISIBLE
            viewConteudo.btFtAddManutencao.hide()
        }
    }

    fun salvarManutencao(manutencao: Manutencao, context: Context) {
        bd = BancoDadoConfig.getInstance(context.applicationContext)
        if(manutencao.idVM != 0L) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    when (manutencao.idM) {
                        0L -> {
                            bd.controleDAO().salvarDadosManutencao(manutencao)
                            withContext(Dispatchers.Main) {
                                controleMensagensSalvar(true, context, manutencao)
                            }
                        }
                        else -> {
                            bd.controleDAO().alterarDadosManutencao(manutencao)
                            withContext(Dispatchers.Main) {
                                controleMensagensSalvar(false, context, manutencao)
                            }
                        }
                    }
                } catch (e: SQLException) {
                    Log.e("ERRO_MANUTENCAO_INSERIR", "ERRO_MANUTENCAO_INSERIR: $e")
                } finally {
                    bd.close()
                }
            }
        } else Toast.makeText(context,"SELECIONE O VEÌCULO", Toast.LENGTH_SHORT).show()
    }

    fun deletarManutencao(
        manutencao: Manutencao,
        context: Context,
        callBack: () -> Unit
    ) {
        bd = BancoDadoConfig.getInstance(context.applicationContext)

        if(manutencao.idM != 0L) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Excluir manutenção")
            builder.setMessage("Deseja excluir essa manutenção?")
            builder.setPositiveButton("SIM") { _, _ ->
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        bd.controleDAO().deletarDadosManutencao(manutencao)
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "MANUTENÇÃO EXCLUIDA", Toast.LENGTH_SHORT).show()
                            callBack.invoke()
                        }
                    } catch (e: SQLException) {
                        Log.e("ERRO_DEL_MANUTENCAO", "ERRO_DELETAR_MANUTECAO: $e")
                    } finally {
                        bd.close()
                    }
                }
            }
            builder.setNegativeButton("NÃO") { _, _ ->
                Toast.makeText(context, "MANUTENÇÃO NÃO SERÁ EXCLUIDA", Toast.LENGTH_SHORT).show()
            }

            builder.setNeutralButton("CANCELAR") { _, _ -> }
            val dialog: AlertDialog = builder.create()
            dialog.show()

        } else {
            Toast.makeText(context, "NÃO POSSUI MANUTENÇÃO", Toast.LENGTH_SHORT).show()
        }
    }

    private fun controleMensagensSalvar(status: Boolean, context: Context, manutencao: Manutencao) {
        when (status) {
            true -> Toast.makeText(context,
                " MANUTENÇÃO SALVA " + manutencao.tipoManutencao.uppercase(Locale.ROOT),
                Toast.LENGTH_SHORT).show()
            false -> Toast.makeText(context,
                " MANUTENÇÃO ALTERADA " + manutencao.tipoManutencao.uppercase(Locale.ROOT),
                Toast.LENGTH_SHORT).show()
        }
    }
}