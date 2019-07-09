package com.hanna.picpaydesafio.apresentacao.contatos

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.hanna.picpaydesafio.R
import hanna.desafiopicpay.Contato
import kotlinx.android.synthetic.main.activity_contatos.*

class ContatosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contatos)

        toolbar_contatos.title = "Contatos"
        setSupportActionBar(toolbar_contatos)

        with(recycler_contatos){
            layoutManager = LinearLayoutManager(this@ContatosActivity, RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
            adapter = ListaContatosAdapter(buscaListaContatos())
        }
    }

    private fun buscaListaContatos(): List<Contato> {
        return listOf(Contato("Nome 1", "Imagem 1", "Username 1"),
                Contato("Nome 2", "Imagem 2", "Username 2"),
                Contato("Nome 3", "Imagem 3", "Username 3"),
                Contato("Nome 4", "Imagem 4", "Username 4"))
    }
}
