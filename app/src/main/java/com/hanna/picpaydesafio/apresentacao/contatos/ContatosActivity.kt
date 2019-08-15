package com.hanna.picpaydesafio.apresentacao.contatos

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.SearchView
import com.hanna.picpaydesafio.R
import com.hanna.picpaydesafio.apresentacao.cartao.CadastroCartaoActivity
import com.hanna.picpaydesafio.apresentacao.cartao.PreCadastroCartaoActivity
import com.hanna.picpaydesafio.apresentacao.pagamento.PagamentoActivity
import com.jakewharton.rxbinding2.widget.queryTextChanges
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_contatos.*

class ContatosActivity : AppCompatActivity() {

    private lateinit var mPreCadastroCartaoActivity: PreCadastroCartaoActivity
    private lateinit var mCadastroCartaoActivity: CadastroCartaoActivity
    private lateinit var mCampoBusca: SearchView
    private val mFundoCampoBuscaAtivo = R.drawable.shape_campo_busca_ativo
    private val mFundoCampoBuscaInativo = R.drawable.shape_campo_busca_inativo
    private var mNumeroCartaoCadastrado: String = ""
    private var mDescarte: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contatos)

        mPreCadastroCartaoActivity = PreCadastroCartaoActivity()
        mCadastroCartaoActivity = CadastroCartaoActivity()
        mCampoBusca = sv_buscaContatos

        manipulaViewModelContatos()
    }

    private fun manipulaViewModelContatos() {
        val viewModelContatos = ViewModelProviders.of(this).get(ContatosViewModel::class.java)
        contatosObserver(viewModelContatos)
        viewModelContatos.buscaListaContatos()
    }

    private fun contatosObserver(viewModelContatos: ContatosViewModel) {
        viewModelContatos.contatoLiveData.observe(this, Observer {
            it?.let { listaContatos ->
                with(rv_listaContatos) {
                    val adapterContatos = ListaContatosAdapter(listaContatos) { contato ->
                        viewModelContatos.gravaDadosContatoSelecionado(this@ContatosActivity, contato)
                        mNumeroCartaoCadastrado = viewModelContatos.buscaNumeroCartaoCadastrado(this@ContatosActivity)
                        defineProximaTela()
                    }
                    verificaMudancaCampoBusca(adapterContatos)
                    layoutManager = LinearLayoutManager(this@ContatosActivity, RecyclerView.VERTICAL, false)
                    setHasFixedSize(true)
                    adapter = adapterContatos
                }
            }
        })
    }

    private fun verificaMudancaCampoBusca(adapterContatos: ListaContatosAdapter) {
        mDescarte = mCampoBusca.queryTextChanges()
            .subscribe { termo ->
                customizaCampoBusca(termo)
                mCampoBusca.onActionViewExpanded()
                adapterContatos.filter.filter(termo)
            }
    }

    private fun customizaCampoBusca(termo: CharSequence?) {
        mCampoBusca.setBackgroundResource(
            if (termo.isNullOrBlank()) mFundoCampoBuscaInativo else mFundoCampoBuscaAtivo
        )
    }

    private fun defineProximaTela() {
        val existeCartaoCadastrado = mNumeroCartaoCadastrado != ""
        val intent: Intent
        intent = if (existeCartaoCadastrado) chamaTelaCadastroCartao() else chamaTelaPreCadastroCartao()
        this.startActivity(intent)
    }

    private fun chamaTelaCadastroCartao() = PagamentoActivity.buscaIntent(this, mNumeroCartaoCadastrado)

    private fun chamaTelaPreCadastroCartao() = PreCadastroCartaoActivity.buscaIntent(this)

    override fun onDestroy() {
        super.onDestroy()
        mDescarte?.dispose()
    }

    override fun onResume() {
        super.onResume()
        with(mCampoBusca) {
            setQuery("", false)
            clearFocus()
        }
    }

    companion object {
        fun buscaIntent(contexto: Context) = Intent(contexto, ContatosActivity::class.java)
    }

}