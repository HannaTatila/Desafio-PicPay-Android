package com.hanna.picpaydesafio.apresentacao.cartao

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import com.hanna.picpaydesafio.dados.modelo.Cartao
import com.hanna.picpaydesafio.dados.persistencia.PreferenciasSeguranca
import com.hanna.picpaydesafio.util.ConstantesPersistencia

class CartaoViewModel : ViewModel() {
    var dadosCartaoLiveData: MutableLiveData<Map<String, String>> = MutableLiveData()
    //var dadosCartaoLiveData: MutableLiveData<Cartao> = MutableLiveData()

    fun armazenaDadosCartao(
        contexto: Context, numeroCartao: String, nomeTitular: String, vencimento: String, cvv: String
    ) {
        val cartao = Cartao(numeroCartao, nomeTitular, vencimento, cvv)
        val cartaoPreferencias = PreferenciasSeguranca(contexto)
        cartaoPreferencias.salvaCartao(cartao)
    }

/*    fun buscaDadosCartao(contexto: Context) {
        val cartao: Cartao? = null
        val cartaoPreferencias = PreferenciasSeguranca(contexto)

        val chaveNumero = ConstantesPersistencia.CHAVE_CARTAO.NUMERO_CARTAO
        val chaveNomeTitular = ConstantesPersistencia.CHAVE_CARTAO.NOME_TITULAR
        val chaveVencimento = ConstantesPersistencia.CHAVE_CARTAO.VENCIMENTO
        val chaveCvv = ConstantesPersistencia.CHAVE_CARTAO.CVV

        cartao?.numero = cartaoPreferencias.buscaValorCartao(chaveNumero)
        cartao?.nome = cartaoPreferencias.buscaValorCartao(chaveNomeTitular)
        cartao?.vencimento = cartaoPreferencias.buscaValorCartao(chaveVencimento)
        cartao?.cvv = cartaoPreferencias.buscaValorCartao(chaveCvv)

        dadosCartaoLiveData.value = cartao

        val cartao: Cartao = Cartao(numero = "", nome = "", vencimento = "", cvv = "")
    }*/

    fun buscaDadosCartao(contexto: Context) {
        val cartaoPreferencias = PreferenciasSeguranca(contexto)
        val cartaoCadastrado = cartaoPreferencias.buscaCartao()

        val dicionarioDadosCartao = mapOf(
            Pair(ConstantesPersistencia.CHAVE_CARTAO.NUMERO_CARTAO, cartaoCadastrado.numero),
            Pair(ConstantesPersistencia.CHAVE_CARTAO.NOME_TITULAR, cartaoCadastrado.nome),
            Pair(ConstantesPersistencia.CHAVE_CARTAO.VENCIMENTO, cartaoCadastrado.vencimento),
            Pair(ConstantesPersistencia.CHAVE_CARTAO.CVV, cartaoCadastrado.cvv)
        )

        dadosCartaoLiveData.value = dicionarioDadosCartao
    }

}