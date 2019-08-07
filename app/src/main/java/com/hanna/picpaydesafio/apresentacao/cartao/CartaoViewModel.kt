package com.hanna.picpaydesafio.apresentacao.cartao

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import com.hanna.picpaydesafio.dados.modelo.Cartao
import com.hanna.picpaydesafio.dados.persistencia.PreferenciasSeguranca

class CartaoViewModel : ViewModel() {
    var dadosCartaoLiveData: MutableLiveData<Cartao> = MutableLiveData()

    fun armazenaDadosCartao(
        contexto: Context, numeroCartao: String, nomeTitular: String, vencimento: String, cvv: String
    ) {
        val cartao = Cartao(numeroCartao, nomeTitular, vencimento, cvv)
        val cartaoPreferencias = PreferenciasSeguranca(contexto)
        cartaoPreferencias.salvaCartao(cartao)
    }

    fun buscaDadosCartao(contexto: Context) {
        val cartaoPreferencias = PreferenciasSeguranca(contexto)
        dadosCartaoLiveData.value = cartaoPreferencias.buscaCartao()
    }

}