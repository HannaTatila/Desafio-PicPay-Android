package com.hanna.picpaydesafio.apresentacao.contatos

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.hanna.picpaydesafio.dados.InicializaRetrofit
import com.hanna.picpaydesafio.dados.modelo.Contato
import com.hanna.picpaydesafio.dados.response.ContatoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Dica: não receber qualquer referência de Activity na ViewModel
class ContatosViewModel : ViewModel() {

    val contatoLiveData: MutableLiveData<List<Contato>> = MutableLiveData()

    fun buscaListaContatos() {
        InicializaRetrofit.contatoServico().buscaListaContatos().enqueue(object : Callback<List<ContatoResponse>?> {
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

        for (item in listaContatosResponse) {
            //id = item.id, nome = item.name, imagem = item.img, username = item.username
            val contato = Contato(item.id, item.name, item.img, item.username)
            listaContatos.add(contato)
        }
        return listaContatos
    }

    // Apenas para testar
    private fun geraListaContatosFake(): List<Contato> {
        return listOf(
                Contato(0, "Nome 1", "Imagem 1", "Username 1"),
                Contato(1, "Nome 2", "Imagem 2", "Username 2"),
                Contato(2, "Nome 3", "Imagem 3", "Username 3"),
                Contato(3, "Nome 4", "Imagem 4", "Username 4")
        )
    }

}