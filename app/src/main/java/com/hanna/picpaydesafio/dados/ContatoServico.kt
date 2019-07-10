package com.hanna.picpaydesafio.dados

import com.hanna.picpaydesafio.dados.response.ContatoResponse
import retrofit2.Call
import retrofit2.http.GET

interface ContatoServico {
    @GET("/tests/mobdev/users")
    fun buscaListaContatos(): Call<List<ContatoResponse>>
}