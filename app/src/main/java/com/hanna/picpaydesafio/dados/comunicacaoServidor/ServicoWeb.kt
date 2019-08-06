package com.hanna.picpaydesafio.dados.comunicacaoServidor

import com.hanna.picpaydesafio.dados.resposta.ContatoResponse
import com.hanna.picpaydesafio.dados.resposta.PagamentoResponse
import com.hanna.picpaydesafio.dados.resposta.TransacaoResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface ServicoWeb {
    @GET("/tests/mobdev/users")
    fun buscaListaContatos(): Call<List<ContatoResponse>>

    @POST("/tests/mobdev/transaction")
    fun finalizaTransacao(@Body pag: PagamentoResponse): Call<TransacaoResponse>
}