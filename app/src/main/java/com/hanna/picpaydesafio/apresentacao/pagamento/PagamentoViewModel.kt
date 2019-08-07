package com.hanna.picpaydesafio.apresentacao.pagamento

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.util.Log
import com.hanna.picpaydesafio.dados.comunicacaoServidor.InicializaRetrofit
import com.hanna.picpaydesafio.dados.modelo.Cartao
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
    var dadosRecebedorLiveData: MutableLiveData<Map<String, String>> = MutableLiveData()
    //var dadosRecebedorLiveData: MutableLiveData<Contato> = MutableLiveData()
    var transacaoLiveData: MutableLiveData<CorpoTransacaoResponse> = MutableLiveData()

    fun buscaDadosRecebedor(contexto: Context) {
        val contatoPreferencias = PreferenciasSeguranca(contexto)

        val recebedor = contatoPreferencias.buscaContato()

        val dicionarioDadosCartao = mapOf(
            Pair(ConstantesPersistencia.CHAVE_CONTATO.ID_CONTATO, recebedor.id.toString()),
            Pair(ConstantesPersistencia.CHAVE_CONTATO.IMG_CONTATO, recebedor.imagem),
            Pair(ConstantesPersistencia.CHAVE_CONTATO.USERNAME_CONTATO, recebedor.username)
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

    fun requisitarTransacao(contexto: Context, valor: BigDecimal) {
        val transacao = criaTransacao(contexto, valor)
        enviaDadosTransacao(transacao)
    }

    private fun criaTransacao(contexto: Context, valor: BigDecimal): PagamentoResponse {
        val cartao = capturaDadosCartao(contexto)
        val idRecebedor =
            dadosRecebedorLiveData.value?.getValue(ConstantesPersistencia.CHAVE_CONTATO.ID_CONTATO)!!.toInt()
        return PagamentoResponse(cartao.numero, cartao.cvv, valor, cartao.vencimento, idRecebedor)
    }

    private fun capturaDadosCartao(contexto: Context): Cartao {
        val cartaoPreferencias = PreferenciasSeguranca(contexto)
        val cartaoCadastrado = cartaoPreferencias.buscaCartao()
        return cartaoCadastrado
    }

    fun enviaDadosTransacao(transacao: PagamentoResponse) {
        InicializaRetrofit.servicoWeb().finalizaTransacao(transacao).enqueue(object : Callback<TransacaoResponse> {
            override fun onResponse(call: Call<TransacaoResponse>, resposta: Response<TransacaoResponse>) {
                if (resposta.isSuccessful) {
                    resposta.body()?.let { transacaoLiveData.value = it.transacao }
                }
            }

            override fun onFailure(call: Call<TransacaoResponse>, t: Throwable) {
                Log.e("Erro onFailure", t.message)
            }
        })
    }

}