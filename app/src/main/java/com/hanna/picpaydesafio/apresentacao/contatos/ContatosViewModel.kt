package com.hanna.picpaydesafio.apresentacao.contatos

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.util.Log
import com.hanna.picpaydesafio.dados.ConstantesPersistencia
import com.hanna.picpaydesafio.dados.InicializaRetrofit
import com.hanna.picpaydesafio.dados.PreferenciasSeguranca
import com.hanna.picpaydesafio.dados.modelo.Contato
import com.hanna.picpaydesafio.dados.response.ContatoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Dica: não receber qualquer referência de Activity na ViewModel
class ContatosViewModel : ViewModel() {

    val contatoLiveData: MutableLiveData<List<Contato>> = MutableLiveData()

    fun buscaListaContatos() {
        InicializaRetrofit.servicoWeb().buscaListaContatos().enqueue(object : Callback<List<ContatoResponse>?> {
            override fun onResponse(call: Call<List<ContatoResponse>?>, response: Response<List<ContatoResponse>?>) {
                if (response.isSuccessful) {
                    response.body()?.let { listaContatosResponse ->
                        contatoLiveData.value = mapeiaContatoResponse(listaContatosResponse)
                    }
                }
            }

            override fun onFailure(call: Call<List<ContatoResponse>?>, t: Throwable) {
                Log.e("Erro onFailure", t.message)
            }
        })
    }

    private fun mapeiaContatoResponse(listaContatosResponse: List<ContatoResponse>): MutableList<Contato> {
        val listaContatos: MutableList<Contato> = mutableListOf()

        listaContatosResponse.forEach { item ->
            val contato = Contato(item.id, item.nome, item.imagem, item.username)
            listaContatos.add(contato)
        }

        return listaContatos
    }

    fun gravaDadosContatoSelecionado(contexto: Context, contato: Contato) {
        val mContatoPreferencias = PreferenciasSeguranca(contexto)

        mContatoPreferencias.armazenaValorContato(
            ConstantesPersistencia.CHAVE_CONTATO.ID_CONTATO,
            contato.id.toString()
        )
        mContatoPreferencias.armazenaValorContato(ConstantesPersistencia.CHAVE_CONTATO.IMG_CONTATO, contato.imagem)
        mContatoPreferencias.armazenaValorContato(
            ConstantesPersistencia.CHAVE_CONTATO.USERNAME_CONTATO,
            contato.username
        )
    }

    fun numeroCartaoCadastrado(contexto: Context): String {
        val mCartaoPreferencias = PreferenciasSeguranca(contexto)
        return mCartaoPreferencias.buscaValorCartao(ConstantesPersistencia.CHAVE_CARTAO.NUMERO_CARTAO)
    }

}