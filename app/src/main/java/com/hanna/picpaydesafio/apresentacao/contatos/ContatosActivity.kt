package com.hanna.picpaydesafio.apresentacao.contatos

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.hanna.picpaydesafio.R
import com.hanna.picpaydesafio.apresentacao.cartao.CadastroCartaoActivity
import com.hanna.picpaydesafio.apresentacao.cartao.PreCadastroCartaoActivity
import com.hanna.picpaydesafio.apresentacao.pagamento.PagamentoActivity
import com.jakewharton.rxbinding2.widget.query
import com.jakewharton.rxbinding2.widget.queryTextChanges
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_contatos.*

class ContatosActivity : AppCompatActivity() {

    private lateinit var mPreCadastroCartaoActivity: PreCadastroCartaoActivity
    private lateinit var mCadastroCartaoActivity: CadastroCartaoActivity
    private var mNumeroCartaoCadastrado: String = ""
    private var mDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contatos)
        mPreCadastroCartaoActivity = PreCadastroCartaoActivity()
        mCadastroCartaoActivity = CadastroCartaoActivity()

        val viewModelContatos = ViewModelProviders.of(this).get(ContatosViewModel::class.java)
        contatosObserver(viewModelContatos)
        viewModelContatos.buscaListaContatos()
    }

    private fun contatosObserver(viewModelContatos: ContatosViewModel) {
        viewModelContatos.contatoLiveData.observe(this, Observer {
            it?.let { contatos ->
                with(recycler_contatos) {
                    val adapterContatos = ListaContatosAdapter(contatos) { contato ->
                        viewModelContatos.gravaDadosContatoSelecionado(this@ContatosActivity, contato)
                        mNumeroCartaoCadastrado = viewModelContatos.buscaNumeroCartaoCadastrado(this@ContatosActivity)
                        //restauraCampoBusca()
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
        //search_contatos.setOnClickListener { onWindowFocusChanged(true) }

        mDisposable = search_contatos.queryTextChanges()
            .skipInitialValue()
            .subscribe { termo ->
                //if (termo.isNullOrBlank()) restauraCampoBusca() else customimzaCampoBusca()
                adapterContatos.filter.filter(termo)
            }
    }


    fun restauraCampoBusca() {
        search_contatos.run {
            clearFocus()
            query(false)
            setOnQueryTextListener(null)
        }
        //search_contatos.clearComposingText()
    }

    private fun defineProximaTela() {
        val existeCartaoCadastrado = mNumeroCartaoCadastrado != ""
        val intent: Intent
        intent = if (existeCartaoCadastrado) {
            PagamentoActivity.buscaIntent(this, mNumeroCartaoCadastrado)
        } else {
            PreCadastroCartaoActivity.buscaIntent(this)
        }
        this.startActivity(intent)
    }

    private fun defineProximaTela2() {
        val existeCartaoCadastrado = mNumeroCartaoCadastrado != ""
        val intent: Intent
        intent = if (existeCartaoCadastrado) chamaTelaCadastroCartao() else chamaTelaPreCadastroCartato()
        this.startActivity(intent)
    }

    private fun chamaTelaCadastroCartao() = PagamentoActivity.buscaIntent(this, mNumeroCartaoCadastrado)

    private fun chamaTelaPreCadastroCartato() = PreCadastroCartaoActivity.buscaIntent(this)


    override fun onDestroy() {
        super.onDestroy()
        mDisposable?.dispose()
    }


}