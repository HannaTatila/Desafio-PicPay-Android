package com.hanna.picpaydesafio.apresentacao.pagamento

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.util.Log
import com.hanna.picpaydesafio.dados.ConstantesPersistencia
import com.hanna.picpaydesafio.dados.InicializaRetrofit
import com.hanna.picpaydesafio.dados.PreferenciasSeguranca
import com.hanna.picpaydesafio.dados.response.CorpoTransacaoResponse
import com.hanna.picpaydesafio.dados.response.PagamentoResponse
import com.hanna.picpaydesafio.dados.response.TransacaoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PagamentoViewModel : ViewModel() {
    private var mNumeroCartao: String = ""
    private var mVencimento: String = ""
    private var mCvv: String = ""
    var dadosRecebedorLiveData: MutableLiveData<Map<String, String>> = MutableLiveData()
    var transacaoLiveData: MutableLiveData<CorpoTransacaoResponse> = MutableLiveData()

    fun buscaDadosRecebedor(contexto: Context) {
        var contatoPreferencias = PreferenciasSeguranca(contexto)
        var chaveIdContato = ConstantesPersistencia.CHAVE_CONTATO.ID_CONTATO
        var chaveImagemContato = ConstantesPersistencia.CHAVE_CONTATO.IMG_CONTATO
        var chaveUsernameContato = ConstantesPersistencia.CHAVE_CONTATO.USERNAME_CONTATO

        var dicionarioDadosRecebedor = mapOf(
            Pair(chaveIdContato, contatoPreferencias.buscaValorContato(chaveIdContato)),
            Pair(chaveImagemContato, contatoPreferencias.buscaValorContato(chaveImagemContato)),
            Pair(chaveUsernameContato, contatoPreferencias.buscaValorContato(chaveUsernameContato))
        )

        dadosRecebedorLiveData.value = dicionarioDadosRecebedor
    }

    fun enviaDadosTransacao(contexto: Context, valor: Double) {
        capturaDadosCartao(contexto)
        val transacao = criaTransacao(valor)

        InicializaRetrofit.servicoWeb().finalizaTransacao(transacao).enqueue(object : Callback<TransacaoResponse> {

            override fun onResponse(call: Call<TransacaoResponse>, response: Response<TransacaoResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { transacaoLiveData.value = it.transacao }
                }
            }

            override fun onFailure(call: Call<TransacaoResponse>, t: Throwable) {
                println("Erro - onFailure - enviaDadosTransacao")
                Log.e("Erro onFailure", t.message)
            }

        })
    }

    private fun criaTransacao(valor: Double): PagamentoResponse {
        val idRecebedor = dadosRecebedorLiveData.value?.get(ConstantesPersistencia.CHAVE_CONTATO.ID_CONTATO)?.toInt()
        return PagamentoResponse(mNumeroCartao, mCvv, valor, mVencimento, idRecebedor!!)
    }

    private fun capturaDadosCartao(contexto: Context) {
        var cartaoPreferencias = PreferenciasSeguranca(contexto)

        mNumeroCartao = cartaoPreferencias.buscaValorCartao(ConstantesPersistencia.CHAVE_CARTAO.NUMERO_CARTAO)
        mVencimento = cartaoPreferencias.buscaValorCartao(ConstantesPersistencia.CHAVE_CARTAO.VENCIMENTO)
        mCvv = cartaoPreferencias.buscaValorCartao(ConstantesPersistencia.CHAVE_CARTAO.CVV)
    }
}