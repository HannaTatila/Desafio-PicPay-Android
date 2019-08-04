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
import android.widget.EditText
import com.hanna.picpaydesafio.R
import com.hanna.picpaydesafio.apresentacao.pagamento.PagamentoActivity
import com.hanna.picpaydesafio.dados.ConstantesPersistencia
import kotlinx.android.synthetic.main.activity_cadastro_cartao.*
import org.jetbrains.anko.toast

class CadastroCartaoActivity : AppCompatActivity() {

    private lateinit var mCartaoViewModel: CartaoViewModel
    private lateinit var mNumeroCartao: EditText
    private lateinit var mTitularCartao: EditText
    private lateinit var mVencimentoCartao: EditText
    private lateinit var mCvvCartao: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_cartao)
        mNumeroCartao = edit_numero_cartao
        mTitularCartao = edit_nome_titular
        mVencimentoCartao = edit_vencimento
        mCvvCartao = edit_cvv

        mCartaoViewModel = ViewModelProviders.of(this).get(CartaoViewModel::class.java)

        verificaSeEAtualizacao()
        capturaEventoAtualizacaoDosCampos()
        capturaEventoCliqueBotao()
    }

    private fun capturaEventoCliqueBotao() {
        botao_voltar_cadastro_cartao.setOnClickListener { onBackPressed() }
        button_salvar_cartao.setOnClickListener { salvarCartao() }
    }

    private fun verificaSeEAtualizacao() {
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
            it?.let { dadosCartao ->
                edit_numero_cartao.setText(dadosCartao[ConstantesPersistencia.CHAVE_CARTAO.NUMERO_CARTAO])
                edit_nome_titular.setText(dadosCartao[ConstantesPersistencia.CHAVE_CARTAO.NOME_TITULAR])
                edit_vencimento.setText(dadosCartao[ConstantesPersistencia.CHAVE_CARTAO.VENCIMENTO])
                edit_cvv.setText(dadosCartao[ConstantesPersistencia.CHAVE_CARTAO.CVV])
            }
        })
    }

    private fun capturaEventoAtualizacaoDosCampos() {
        var listaCamposEditText =
            listOf<TextInputEditText>(edit_numero_cartao, edit_nome_titular, edit_vencimento, edit_cvv)

        for (editText in listaCamposEditText) {
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val camposValidos = verificaTodosCamposPreenchidos()
                    customizaBotao(camposValidos)
                }
            })
        }
    }

    private fun verificaTodosCamposPreenchidos(): Boolean {
        return edit_numero_cartao.text.toString() != ""
                && edit_nome_titular.text.toString() != ""
                && edit_vencimento.text.toString() != ""
                && edit_cvv.text.toString() != ""
    }

    private fun customizaBotao(camposValidos: Boolean) {
        if (camposValidos) button_salvar_cartao.visibility = View.VISIBLE
        else button_salvar_cartao.visibility = View.GONE
    }

    private fun salvarCartao() {
        try {
            val numeroCartao = edit_numero_cartao.text.toString()
            val nomeTitular = edit_nome_titular.text.toString()
            val vencimento = edit_vencimento.text.toString()
            val cvv = edit_cvv.text.toString()

            mCartaoViewModel.armazenaDadosCartao(this, numeroCartao, nomeTitular, vencimento, cvv)
            toast(getString(R.string.msg_sucesso_cadastro))
            chamaTelaPagamento(numeroCartao)
        } catch (e: Exception) {
            println(e.message.toString())
            toast(getString(R.string.msg_erro_inesperado))
        }
    }

    private fun chamaTelaPagamento(numeroCartao: String) {
        val intent = PagamentoActivity.buscaIntent(this, numeroCartao)
        this.startActivity(intent)
    }

    companion object {
        private const val NUMERO_CARTAO = "NUMERO_CARTAO" //TODO: criar constante

        fun buscaIntent(contexto: Context, numeroCartao: String?): Intent {
            val pacote = Bundle()
            pacote.putString(NUMERO_CARTAO, numeroCartao)
            return Intent(contexto, CadastroCartaoActivity::class.java).putExtras(pacote)
        }
    }

}

