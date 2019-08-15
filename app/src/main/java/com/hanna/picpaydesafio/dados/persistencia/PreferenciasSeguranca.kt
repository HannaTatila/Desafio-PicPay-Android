package com.hanna.picpaydesafio.dados.persistencia

import android.content.Context
import android.content.SharedPreferences
import com.hanna.picpaydesafio.dados.modelo.Cartao
import com.hanna.picpaydesafio.dados.modelo.Contato
import com.hanna.picpaydesafio.util.ConstantesPersistencia

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class PreferenciasSeguranca(contexto: Context) {

    private val mContatoPreferencias: SharedPreferences =
        contexto.getSharedPreferences("contatosPicpay", Context.MODE_PRIVATE)

    private val mCartaoPreferencias: SharedPreferences =
        contexto.getSharedPreferences("cartaoPicpay", Context.MODE_PRIVATE)


    fun salvaContato(contato: Contato) {
        armazenaValorContato(ConstantesPersistencia.CHAVE_CONTATO.ID_CONTATO, contato.id.toString())
        armazenaValorContato(ConstantesPersistencia.CHAVE_CONTATO.IMG_CONTATO, contato.imagem)
        armazenaValorContato(ConstantesPersistencia.CHAVE_CONTATO.USERNAME_CONTATO, contato.username)
    }

    fun buscaContato(): Contato {
        val id = buscaValorContato(ConstantesPersistencia.CHAVE_CONTATO.ID_CONTATO)
        val imagem = buscaValorContato(ConstantesPersistencia.CHAVE_CONTATO.IMG_CONTATO)
        val username = buscaValorContato(ConstantesPersistencia.CHAVE_CONTATO.USERNAME_CONTATO)

        val contatoSelecionado = Contato(id.toInt(), imagem = imagem, username = username)
        return contatoSelecionado
    }

    fun removeContato() {
        removeValorContato(ConstantesPersistencia.CHAVE_CONTATO.ID_CONTATO)
        removeValorContato(ConstantesPersistencia.CHAVE_CONTATO.IMG_CONTATO)
        removeValorContato(ConstantesPersistencia.CHAVE_CONTATO.USERNAME_CONTATO)
    }

    fun armazenaValorContato(chave: String, valor: String) {
        mContatoPreferencias.edit().putString(chave, valor).apply()
    }

    fun buscaValorContato(chave: String): String {
        return mContatoPreferencias.getString(chave, "")
    }

    fun removeValorContato(chave: String) {
        mContatoPreferencias.edit().remove(chave).apply()
    }

    fun salvaCartao(cartao: Cartao) {
        armazenaValorCartao(ConstantesPersistencia.CHAVE_CARTAO.NUMERO_CARTAO, cartao.numero)
        armazenaValorCartao(ConstantesPersistencia.CHAVE_CARTAO.NOME_TITULAR, cartao.nome)
        armazenaValorCartao(ConstantesPersistencia.CHAVE_CARTAO.VENCIMENTO, cartao.vencimento)
        armazenaValorCartao(ConstantesPersistencia.CHAVE_CARTAO.CVV, cartao.cvv)
    }

    fun buscaCartao(): Cartao {
        val numero = buscaValorCartao(ConstantesPersistencia.CHAVE_CARTAO.NUMERO_CARTAO)
        val nomeTitular = buscaValorCartao(ConstantesPersistencia.CHAVE_CARTAO.NOME_TITULAR)
        val vencimento = buscaValorCartao(ConstantesPersistencia.CHAVE_CARTAO.VENCIMENTO)
        val cvv = buscaValorCartao(ConstantesPersistencia.CHAVE_CARTAO.CVV)

        val cartaoCadastrado = Cartao(numero, nomeTitular, vencimento, cvv)
        return cartaoCadastrado
    }

    fun armazenaValorCartao(chave: String, valor: String) {
        mCartaoPreferencias.edit().putString(chave, valor).apply()

    }

    fun buscaValorCartao(chave: String): String {
        return mCartaoPreferencias.getString(chave, "")
    }

}