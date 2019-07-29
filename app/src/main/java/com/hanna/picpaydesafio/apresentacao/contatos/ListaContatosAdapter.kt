package com.hanna.picpaydesafio.apresentacao.contatos

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.hanna.picpaydesafio.R
import com.hanna.picpaydesafio.dados.modelo.Contato
import kotlinx.android.synthetic.main.linha_lista_contatos.view.*

class ListaContatosAdapter(
    private val contatos: List<Contato>,
    private val cliqueItemLista: ((contato: Contato) -> Unit)
) : RecyclerView.Adapter<ListaContatosAdapter.ListaContatosViewHolder>() {

    override fun onCreateViewHolder(pai: ViewGroup, viewTipo: Int): ListaContatosViewHolder {
        val contexto = pai.context
        val view = LayoutInflater.from(contexto).inflate(R.layout.linha_lista_contatos, pai, false)

        return ListaContatosViewHolder(view, cliqueItemLista, contexto)
    }

    override fun getItemCount(): Int {
        return contatos.count()
    }

    override fun onBindViewHolder(contatosViewHolder: ListaContatosViewHolder, posicao: Int) {
        contatosViewHolder.incorporaDados(contatos[posicao])
    }

    class ListaContatosViewHolder(
        itemView: View, private val cliqueItemLista: ((contato: Contato) -> Unit), private val contexto: Context
    ) : RecyclerView.ViewHolder(itemView) {

        private val imagem = itemView.image_foto_contato
        private val username = itemView.text_username_contato
        private val nome = itemView.text_nome_contato

        fun incorporaDados(contato: Contato) {
            Glide.with(contexto).load(contato.imagem).into(imagem)
            username.text = contato.username
            nome.text = contato.nome

            itemView.setOnClickListener { cliqueItemLista.invoke(contato) }
        }

    }
}