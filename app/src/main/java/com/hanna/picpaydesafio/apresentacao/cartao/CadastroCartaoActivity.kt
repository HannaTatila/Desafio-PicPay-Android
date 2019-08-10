package com.hanna.picpaydesafio.apresentacao.cartao

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import com.hanna.picpaydesafio.R
import com.hanna.picpaydesafio.apresentacao.pagamento.PagamentoActivity
import com.hanna.picpaydesafio.util.ConstantesPacoteIntent
import kotlinx.android.synthetic.main.activity_cadastro_cartao.*
import org.jetbrains.anko.toast

class CadastroCartaoActivity : AppCompatActivity() {

    private lateinit var mCartaoViewModel: CartaoViewModel
    private lateinit var mCampoNumeroCartao: TextInputEditText
    private lateinit var mCampoTitularCartao: TextInputEditText
    private lateinit var mCampoVencimentoCartao: TextInputEditText
    private lateinit var mCampoCvvCartao: TextInputEditText
    private lateinit var mBotaoSalvar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_cartao)
        capturaCamposLayout()

        mCartaoViewModel = ViewModelProviders.of(this).get(CartaoViewModel::class.java)

        buscaDadosCasoSejaAtualizacao()
        capturaEventoAtualizacaoDosCampos()
        capturaEventoCliqueBotao()
    }

    private fun capturaCamposLayout() {
        mCampoNumeroCartao = tiet_numeroCartao
        mCampoTitularCartao = tiet_nomeTitular
        mCampoVencimentoCartao = tiet_vencimento
        mCampoCvvCartao = tiet_cvv
        mBotaoSalvar = btn_salvarCartao
    }

    private fun buscaDadosCasoSejaAtualizacao() {
        val existeCartaoCadastrado = verificaExistenciaCartao()
        if (existeCartaoCadastrado) {
            mCartaoViewModel.buscaDadosCartao(this)
            cartaoObserver()
        }
    }

    private fun verificaExistenciaCartao(): Boolean {
        val pacote = intent.extras
        return pacote != null
    }

    private fun cartaoObserver() {
        mCartaoViewModel.dadosCartaoLiveData.observe(this, Observer {
            it?.let { cartaoCadastrado ->
                mCampoNumeroCartao.setText(cartaoCadastrado.numero)
                mCampoTitularCartao.setText(cartaoCadastrado.nome)
                mCampoVencimentoCartao.setText(cartaoCadastrado.vencimento)
                mCampoCvvCartao.setText(cartaoCadastrado.cvv)
            }
        })
    }

    private fun capturaEventoAtualizacaoDosCampos() {
        val listaCamposEditText =
            listOf(mCampoNumeroCartao, mCampoTitularCartao, mCampoVencimentoCartao, mCampoCvvCartao)

        for (campo in listaCamposEditText) {
            campo.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    customizaBotao()
                }
            })
        }
    }

    private fun customizaBotao() {
        val camposValidos = verificaSeTodosCamposEstaoPreenchidos()
        if (camposValidos) mBotaoSalvar.visibility = View.VISIBLE
        else mBotaoSalvar.visibility = View.GONE
    }

    private fun verificaSeTodosCamposEstaoPreenchidos(): Boolean {
        return mCampoNumeroCartao.text.toString() != ""
                && mCampoTitularCartao.text.toString() != ""
                && mCampoVencimentoCartao.text.toString() != ""
                && mCampoCvvCartao.text.toString() != ""
    }

    private fun capturaEventoCliqueBotao() {
        ib_botaoVoltarCadastro.setOnClickListener { onBackPressed() }
        mBotaoSalvar.setOnClickListener { salvarCartao() }
    }

    private fun salvarCartao() {
        try {
            val numeroCartao = mCampoNumeroCartao.text.toString()
            val nomeTitular = mCampoTitularCartao.text.toString()
            val vencimento = mCampoVencimentoCartao.text.toString()
            val cvv = mCampoCvvCartao.text.toString()

            mCartaoViewModel.armazenaDadosCartao(this, numeroCartao, nomeTitular, vencimento, cvv)
            toast(getString(R.string.msg_sucesso_cadastro))
            chamaTelaPagamento(numeroCartao)
        } catch (e: Exception) {
            toast(getString(R.string.msg_erro_inesperado))
        }
    }

    private fun chamaTelaPagamento(numeroCartao: String) {
        val intent = PagamentoActivity.buscaIntent(this, numeroCartao)
        this.startActivity(intent)
    }

    companion object {
        fun buscaIntent(contexto: Context, numeroCartao: String?): Intent {
            val pacote = Bundle()
            pacote.putString(ConstantesPacoteIntent.CHAVE_CARTAO.NUMERO_CARTAO, numeroCartao)
            return Intent(contexto, CadastroCartaoActivity::class.java).putExtras(pacote)
        }
    }

}

