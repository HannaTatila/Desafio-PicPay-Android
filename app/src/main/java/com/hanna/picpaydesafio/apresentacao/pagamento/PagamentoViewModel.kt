package com.hanna.picpaydesafio.apresentacao.pagamento

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.util.Log
import com.hanna.picpaydesafio.dados.comunicacaoServidor.InicializaRetrofit
import com.hanna.picpaydesafio.dados.persistencia.PreferenciasSeguranca
import com.hanna.picpaydesafio.dados.resposta.CorpoTransacaoResponse
import com.hanna.picpaydesafio.dados.resposta.PagamentoResponse
import com.hanna.picpaydesafio.dados.resposta.TransacaoResponse
import com.hanna.picpaydesafio.util.ConstantesPersistencia
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal


class PagamentoViewModel : ViewModel() {
    private var mNumeroCartao: String = ""
    private var mVencimento: String = ""
    private var mCvv: String = ""
    var dadosRecebedorLiveData: MutableLiveData<Map<String, String>> = MutableLiveData()
    //var dadosRecebedorLiveData: MutableLiveData<Contato> = MutableLiveData()
    var transacaoLiveData: MutableLiveData<CorpoTransacaoResponse> = MutableLiveData()

    fun buscaDadosRecebedor(contexto: Context) {
        val cartaoPreferencias = PreferenciasSeguranca(contexto)

        val chaveIdContato = ConstantesPersistencia.CHAVE_CONTATO.ID_CONTATO
        val chaveImagemContato = ConstantesPersistencia.CHAVE_CONTATO.IMG_CONTATO
        val chaveUsernameContato = ConstantesPersistencia.CHAVE_CONTATO.USERNAME_CONTATO

        val dicionarioDadosCartao = mapOf(
            Pair(chaveIdContato, cartaoPreferencias.buscaValorContato(chaveIdContato)),
            Pair(chaveImagemContato, cartaoPreferencias.buscaValorContato(chaveImagemContato)),
            Pair(chaveUsernameContato, cartaoPreferencias.buscaValorContato(chaveUsernameContato))
        )

        dadosRecebedorLiveData.value = dicionarioDadosCartao
    }
/*        var contato: Contato? = null

        val contatoPreferencias = PreferenciasSeguranca(contexto)
        val chaveIdContato = ConstantesPersistencia.CHAVE_CONTATO.ID_CONTATO
        val chaveImagemContato = ConstantesPersistencia.CHAVE_CONTATO.IMG_CONTATO
        val chaveUsernameContato = ConstantesPersistencia.CHAVE_CONTATO.USERNAME_CONTATO

        contato?.id = contatoPreferencias.buscaValorContato(chaveIdContato).toInt()
        contato?.imagem = contatoPreferencias.buscaValorContato(chaveImagemContato)
        contato?.username = contatoPreferencias.buscaValorContato(chaveUsernameContato)

        dadosRecebedorLiveData.value = contato
    }*/

    fun enviaDadosTransacao(contexto: Context, valor: BigDecimal) {
        capturaDadosCartao(contexto)
        val transacao = criaTransacao(valor)

        InicializaRetrofit.servicoWeb().finalizaTransacao(transacao).enqueue(object : Callback<TransacaoResponse> {
            override fun onResponse(call: Call<TransacaoResponse>, response: Response<TransacaoResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { transacaoLiveData.value = it.transacao }
                }
            }

            override fun onFailure(call: Call<TransacaoResponse>, t: Throwable) {
                Log.e("Erro onFailure", t.message)
            }
        })
    }

    private fun criaTransacao(valor: BigDecimal): PagamentoResponse {
        val idRecebedor =
            dadosRecebedorLiveData.value?.getValue(ConstantesPersistencia.CHAVE_CONTATO.ID_CONTATO)!!.toInt()
        // TODO: analisar se eh pagamento ou pagamentoResponse
        return PagamentoResponse(mNumeroCartao, mCvv, valor, mVencimento, idRecebedor)
    }

    //TODO: criar metodo generico no SharedPreferences
    private fun capturaDadosCartao(contexto: Context) {
        val cartaoPreferencias = PreferenciasSeguranca(contexto)

        mNumeroCartao = cartaoPreferencias.buscaValorCartao(ConstantesPersistencia.CHAVE_CARTAO.NUMERO_CARTAO)
        mVencimento = cartaoPreferencias.buscaValorCartao(ConstantesPersistencia.CHAVE_CARTAO.VENCIMENTO)
        mCvv = cartaoPreferencias.buscaValorCartao(ConstantesPersistencia.CHAVE_CARTAO.CVV)
    }

/*
    private fun capturaDadosCartao(contexto: Context) {

        mNumeroCartao = cartaoPreferencias.buscaValorCartao(ConstantesPersistencia.CHAVE_CARTAO.NUMERO_CARTAO)
        mVencimento = cartaoPreferencias.buscaValorCartao(ConstantesPersistencia.CHAVE_CARTAO.VENCIMENTO)
        mCvv = cartaoPreferencias.buscaValorCartao(ConstantesPersistencia.CHAVE_CARTAO.CVV)

        return Cartao()
    }
    */
}