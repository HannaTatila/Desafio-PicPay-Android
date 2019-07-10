package com.hanna.picpaydesafio.dados

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// A notação object torna a classe um Singleton
object InicializaRetrofit {
    private val retrofit = Retrofit.Builder()
            .baseUrl("http://careers.picpay.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun contatoServico(): ContatoServico {
        return retrofit.create(ContatoServico::class.java)
    }

}