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
import kotlinx.android.synthetic.main.item_lista_contatos.view.*

class ListaContatosAdapter(
    private val listaContatosGeral: List<Contato>,
    private val cliqueItemLista: ((contato: Contato) -> Unit)
) : RecyclerView.Adapter<ListaContatosAdapter.ListaContatosViewHolder>(), Filterable {

    private var mListaContatos = listaContatosGeral

    override fun onCreateViewHolder(pai: ViewGroup, viewTipo: Int): ListaContatosViewHolder {
        val contexto = pai.context
        val view = LayoutInflater.from(contexto).inflate(R.layout.item_lista_contatos, pai, false)

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

        private val campoImagem = itemView.civ_fotoContato
        private val campoUsername = itemView.tv_usernameContato
        private val campoNome = itemView.tv_nomeContato

        fun incorporaDados(contato: Contato) {
            Glide.with(contexto).load(contato.imagem).into(campoImagem)
            campoUsername.text = contato.username
            campoNome.text = contato.nome

            itemView.setOnClickListener { cliqueItemLista.invoke(contato) }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(entradaFiltro: CharSequence?): FilterResults {
                var listaFiltrada: List<Contato> = ArrayList()

                if (entradaFiltro != null) {
                    val termoDigitado = entradaFiltro.toString().trim().toLowerCase()
                    listaFiltrada = listaContatosGeral.filter { contato ->
                        contato.nome.toLowerCase().contains(termoDigitado) or
                                contato.username.toLowerCase().contains(termoDigitado)
                    }
                }
                return criaResutadoFiltro(listaFiltrada)
            }

            private fun criaResutadoFiltro(listaFiltrada: List<Contato>): FilterResults {
                val resultadoFiltro = FilterResults()
                with(resultadoFiltro) {
                    values = listaFiltrada
                    count = listaFiltrada.size
                }
                return resultadoFiltro
            }

            override fun publishResults(entradaFiltro: CharSequence?, resultadoFiltro: FilterResults?) {
                mListaContatos = resultadoFiltro?.values as List<Contato>
                notifyDataSetChanged()
            }
        }
    }

}