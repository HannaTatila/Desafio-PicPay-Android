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
import kotlinx.android.synthetic.main.activity_contatos.*

class ContatosActivity : AppCompatActivity() {

    private lateinit var mPreCadastroCartaoActivity: PreCadastroCartaoActivity
    private lateinit var mCadastroCartaoActivity: CadastroCartaoActivity
    private var mNumeroCartaoCadastrado: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contatos)
        mPreCadastroCartaoActivity = PreCadastroCartaoActivity()
        mCadastroCartaoActivity = CadastroCartaoActivity()

        val viewModelContatos = ViewModelProviders.of(this).get(ContatosViewModel::class.java)
        contatosObserver(viewModelContatos)
        viewModelContatos.buscaListaContatos()

        search_contatos.setOnClickListener { search_contatos.setBackgroundResource(R.drawable.shape_campo_busca_ativo) }
    }

    private fun contatosObserver(viewModelContatos: ContatosViewModel) {
        viewModelContatos.contatoLiveData.observe(this, Observer {
            it?.let { contatos ->
                with(recycler_contatos) {
                    layoutManager = LinearLayoutManager(this@ContatosActivity, RecyclerView.VERTICAL, false)
                    setHasFixedSize(true)
                    adapter = ListaContatosAdapter(contatos) { contato ->
                        viewModelContatos.gravaDadosContatoSelecionado(this@ContatosActivity, contato)
                        mNumeroCartaoCadastrado = viewModelContatos.numeroCartaoCadastrado(this@ContatosActivity)
                        defineProximaTela()
                    }
                }
            }
        })
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

}