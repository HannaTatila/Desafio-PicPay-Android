package com.hanna.picpaydesafio.apresentacao.pagamento

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import com.bumptech.glide.Glide
import com.hanna.picpaydesafio.R
import com.hanna.picpaydesafio.apresentacao.cartao.CadastroCartaoActivity
import com.hanna.picpaydesafio.util.ConstantesPacotes
import kotlinx.android.synthetic.main.activity_pagamento.*
import kotlinx.android.synthetic.main.include_botao_voltar.*
import me.abhinay.input.CurrencyEditText
import org.jetbrains.anko.toast
import java.math.BigDecimal


class PagamentoActivity : AppCompatActivity() {

    private lateinit var mPagamentoViewModel: PagamentoViewModel
    private var mNumeroCartao: String? = ""
    private var mNumeroCartaoProtegido: String = ""
    private var mUrlImagemContato: String = ""
    private var mUsernameContato: String = ""
    private lateinit var mCampoValorPagamento: CurrencyEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagamento)
        mCampoValorPagamento = cet_valorPagamento

        capturaNumeroCartao()
        protegeNumeroCartao()
        manipulaPagamentoViewModel()
        capturaEventoAtualizacaoValor()
        controlaEventoClique()
    }

    private fun capturaNumeroCartao() {
        val pacote = intent.extras
        if (pacote != null) {
            mNumeroCartao = pacote.getString(ConstantesPacotes.CHAVE_CARTAO.NUMERO_CARTAO)
        }
    }

    private fun protegeNumeroCartao() {
        mNumeroCartaoProtegido = mNumeroCartao!!.take(4)
    }

    private fun manipulaPagamentoViewModel() {
        mPagamentoViewModel = ViewModelProviders.of(this).get(PagamentoViewModel::class.java).also { pagViewModel ->
            pagViewModel.buscaDadosRecebedor(this)
            recebedorObserver(pagViewModel)
            transacaoObserver(pagViewModel)
        }
    }

    private fun recebedorObserver(mPagamentoViewModel: PagamentoViewModel) {
        mPagamentoViewModel.dadosRecebedorLiveData.observe(this, Observer {
            it?.let { recebedor ->
                mUrlImagemContato = recebedor.imagem
                mUsernameContato = recebedor.username
                incorporaDadosView()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun incorporaDadosView() {
        Glide.with(this@PagamentoActivity).load(mUrlImagemContato).into(civ_fotoContato)
        tv_usernameContato.text = mUsernameContato
        tv_numeroCartao.text = getString(R.string.mastercard) + " " + mNumeroCartaoProtegido + " •"
    }

    private fun capturaEventoAtualizacaoValor() {
        mCampoValorPagamento.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                configuraComponentesTela()
            }
        })
    }

    private fun configuraComponentesTela() {
        val valorFoiPreenchido = verificaValorPreenchido()
        if (valorFoiPreenchido) customizaCores(R.color.corVerdeFonte) else customizaCores(R.color.corHintCinzaEscuro)
        btn_pagar.isEnabled = valorFoiPreenchido
    }

    private fun verificaValorPreenchido(): Boolean {
        val conteudoCampoValor = mCampoValorPagamento.text.toString()
        return conteudoCampoValor != "0,00"
    }

    private fun customizaCores(corTexto: Int) {
        val cor = ContextCompat.getColor(this@PagamentoActivity, corTexto)
        mCampoValorPagamento.setTextColor(cor)
        tv_cifrao.setTextColor(cor)
    }

    private fun controlaEventoClique() {
        ib_botaoVoltar.setOnClickListener { onBackPressed() }

        tv_linkEditarCartao.setOnClickListener { chamaTelaCadastroCartao() }

        btn_pagar.setOnClickListener {
            val valorPagamento = formataParaBigDecimal()
            mPagamentoViewModel.requisitarTransacao(this, valorPagamento)
        }
    }

    private fun formataParaBigDecimal(): BigDecimal {
        val valorFormatado = mCampoValorPagamento.text.toString()
            .replace(".", "", true)
            .replace(",", ".", true)
        return valorFormatado.toBigDecimal()
    }

    private fun transacaoObserver(mPagamentoViewModel: PagamentoViewModel) {
        mPagamentoViewModel.transacaoLiveData.observe(this, Observer {
            it?.let { transacao ->
                if (transacao.sucesso) {
                    toast(getString(R.string.mag_sucesso_pagamento))
                    geraRecibo(transacao.id, transacao.valor, transacao.timestamp)
                } else {
                    toast(getString(R.string.mag_recusa_pagamento))
                }
            }
        })
    }

    private fun geraRecibo(idTransacao: Int, valor: BigDecimal, timestamp: String) {
        val recibo = ReciboBottomSheetFragment()
        recibo.arguments = coletaValoresRecibo(idTransacao, valor, timestamp)
        recibo.show(supportFragmentManager, recibo.tag)
    }

    private fun coletaValoresRecibo(idTransacao: Int, valor: BigDecimal, timestamp: String): Bundle {
        val pacoteTransacao = Bundle()
        pacoteTransacao.putString(ConstantesPacotes.CHAVE_TRANSACAO.ID_TRANSACAO, idTransacao.toString())
        pacoteTransacao.putString(ConstantesPacotes.CHAVE_TRANSACAO.VALOR_TRANSACAO, valor.toString())
        pacoteTransacao.putString(ConstantesPacotes.CHAVE_TRANSACAO.TIMESTAMP_TRANSACAO, timestamp)
        pacoteTransacao.putString(ConstantesPacotes.CHAVE_TRANSACAO.FOTO_TRANSACAO, mUrlImagemContato)
        pacoteTransacao.putString(ConstantesPacotes.CHAVE_TRANSACAO.USERNAME_TRANSACAO, mUsernameContato)
        pacoteTransacao.putString(ConstantesPacotes.CHAVE_TRANSACAO.CARTAO_TRANSACAO, mNumeroCartaoProtegido)

        return pacoteTransacao
    }

    private fun chamaTelaCadastroCartao() {
        val intent = CadastroCartaoActivity.buscaIntent(this, mNumeroCartao)
        this.startActivity(intent)
    }

    companion object {
        fun buscaIntent(contexto: Context, numeroCartao: String): Intent {
            val pacote = Bundle()
            pacote.putString(ConstantesPacotes.CHAVE_CARTAO.NUMERO_CARTAO, numeroCartao)
            return Intent(contexto, PagamentoActivity::class.java).putExtras(pacote)
        }
    }
}
