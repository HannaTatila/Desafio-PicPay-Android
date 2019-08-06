package com.hanna.picpaydesafio.dados.persistencia

import android.content.Context
import android.content.SharedPreferences

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class PreferenciasSeguranca(contexto: Context) {

    private val mContatoSharedPreferences: SharedPreferences =
        contexto.getSharedPreferences("contatosPicpay", Context.MODE_PRIVATE)

    private val mCartaoSharedPreferences: SharedPreferences =
        contexto.getSharedPreferences("cartaoPicpay", Context.MODE_PRIVATE)

    fun armazenaValorContato(chave: String, valor: String) {
        mContatoSharedPreferences.edit().putString(chave, valor).apply()
    }

    fun buscaValorContato(chave: String): String {
        return mContatoSharedPreferences.getString(chave, "")
    }

    fun armazenaValorCartao(chave: String, valor: String) {
        mCartaoSharedPreferences.edit().putString(chave, valor).apply()

    }

    fun buscaValorCartao(chave: String): String {
        return mCartaoSharedPreferences.getString(chave, "")
    }
}