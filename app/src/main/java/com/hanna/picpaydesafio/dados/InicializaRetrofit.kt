package com.hanna.picpaydesafio.dados

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object InicializaRetrofit {
    private val retrofit = Retrofit.Builder()
            .baseUrl("http://careers.picpay.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun servicoWeb(): ServicoWeb {
        return retrofit.create(ServicoWeb::class.java)
    }

}