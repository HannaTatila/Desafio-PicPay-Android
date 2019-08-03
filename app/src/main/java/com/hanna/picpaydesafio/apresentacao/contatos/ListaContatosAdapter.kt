package com.hanna.picpaydesafio.apresentacao.contatos

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.bumptech.glide.Glide
import com.hanna.picpaydesafio.R
import com.hanna.picpaydesafio.dados.modelo.Contato
import kotlinx.android.synthetic.main.linha_lista_contatos.view.*

class ListaContatosAdapter(
    private val contatosGeral: List<Contato>,
    private val cliqueItemLista: ((contato: Contato) -> Unit)
) : RecyclerView.Adapter<ListaContatosAdapter.ListaContatosViewHolder>(), Filterable {

    private var mListaContatos: List<Contato>
    private var mContatosFiltro: Filter

    init {
        this.mListaContatos = contatosGeral
        this.mContatosFiltro = ContatosFiltro()
    }

    override fun onCreateViewHolder(pai: ViewGroup, viewTipo: Int): ListaContatosViewHolder {
        val contexto = pai.context
        val view = LayoutInflater.from(contexto).inflate(R.layout.linha_lista_contatos, pai, false)

        return ListaContatosViewHolder(view, cliqueItemLista, contexto)
    }

    override fun getItemCount(): Int {
        return mListaContatos.count()
    }

    override fun onBindViewHolder(contatosViewHolder: ListaContatosViewHolder, posicao: Int) {
        contatosViewHolder.incorporaDados(mListaContatos[posicao])
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

    override fun getFilter(): Filter = mContatosFiltro


    private inner class ContatosFiltro : Filter() {
        override fun performFiltering(caracteres: CharSequence?): FilterResults {
            var listaFiltrada: List<Contato> = ArrayList()
            if (caracteres != null) {
                val termo = caracteres.toString().trim().toLowerCase()
                listaFiltrada = contatosGeral.filter { contato ->
                    contato.nome.toLowerCase().contains(termo) or contato.username.toLowerCase().contains(termo)
                }
            }

            val resultadoFiltro = FilterResults()
            with(resultadoFiltro) {
                values = listaFiltrada
                count = listaFiltrada.size
            }
            return resultadoFiltro
        }

        override fun publishResults(caracteres: CharSequence?, resultadoFiltro: FilterResults?) {
            mListaContatos = resultadoFiltro?.values as List<Contato>
            notifyDataSetChanged()
        }
    }


}