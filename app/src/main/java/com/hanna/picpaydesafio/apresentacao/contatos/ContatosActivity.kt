package com.hanna.picpaydesafio.apresentacao.contatos

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.hanna.picpaydesafio.R
import com.hanna.picpaydesafio.apresentacao.cartao.PreCadastroCartaoActivity
import kotlinx.android.synthetic.main.activity_contatos.*

class ContatosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contatos)

        intitulaToolbar()

        val viewModel: ContatosViewModel = ViewModelProviders.of(this).get(ContatosViewModel::class.java)
        contatosObserver(viewModel)
        viewModel.buscaListaContatos()
    }

    private fun contatosObserver(viewModel: ContatosViewModel) {
        viewModel.contatoLiveData.observe(this, Observer {
            it?.let { contatos ->
                with(recycler_contatos) {
                    layoutManager = LinearLayoutManager(this@ContatosActivity, RecyclerView.VERTICAL, false)
                    setHasFixedSize(true)
                    adapter = ListaContatosAdapter(contatos) { contato ->
                        val intent = PreCadastroCartaoActivity.buscaIntent(
                                this@ContatosActivity, contato.nome, contato.imagem, contato.username
                        )
                        this@ContatosActivity.startActivity(intent)
                    }
                }
            }
        })
    }

    private fun intitulaToolbar() {
        toolbar_contatos.title = "Contatos"
        setSupportActionBar(toolbar_contatos)
    }
}
