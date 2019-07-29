package com.hanna.picpaydesafio.apresentacao.cartao

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.hanna.picpaydesafio.R
import com.hanna.picpaydesafio.apresentacao.base.BaseActivity
import com.hanna.picpaydesafio.apresentacao.pagamento.PagamentoActivity
import com.hanna.picpaydesafio.dados.ConstantesPersistencia
import kotlinx.android.synthetic.main.activity_cadastro_cartao.*
import kotlinx.android.synthetic.main.include_toolbar.*
import org.jetbrains.anko.toast

class CadastroCartaoActivity : BaseActivity() {

    lateinit var mCartaoViewModel: CartaoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_cartao)
        configuraToolBar(toolbar_geral)

        mCartaoViewModel = ViewModelProviders.of(this).get(CartaoViewModel::class.java)
        cartaoObserver()
        mCartaoViewModel.buscaDadosCartao(this)

        capturaEventoAtualizacaoDosCampos()
        capturaEventoCliqueBotao()

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

    private fun capturaEventoCliqueBotao() {
        button_salvar_cartao.setOnClickListener {
            salvarCartao()
        }
    }

    private fun salvarCartao() {
        //if (valoresValidos()) {
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
        //}else{
        //    toast("Preencha os campos corretamente")
        //}
    }

    private fun capturaEventoAtualizacaoDosCampos() {
        var listaEditTextCampos =
            listOf<TextInputEditText>(edit_numero_cartao, edit_nome_titular, edit_vencimento, edit_cvv)

        for (editText in listaEditTextCampos) {
            editText.addTextChangedListener(object : TextWatcher {
                //var atualizado = false
                override fun afterTextChanged(s: Editable?) {}

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    /*if (atualizado) {
                        atualizado = false
                        return
                    }*/
                    verificaTodosCamposPreenchidos()
                    //atualizado = true
                }
            })
        }
    }


    private fun chamaTelaPagamento(numeroCartao: String) {
        val intent = PagamentoActivity.buscaIntent(this, numeroCartao)
        this.startActivity(intent)
        finish()
    }

    /* Usar essa função apenas se for usar o banco de dados
    override fun onDestroy() {
        mCartaoViewModel.destroiIntancia()
        super.onDestroy()
    }
    */

    private fun verificaTodosCamposPreenchidos() {
        if (edit_numero_cartao.text.toString() != ""
            && edit_nome_titular.text.toString() != ""
            && edit_vencimento.text.toString() != ""
            && edit_cvv.text.toString() != ""
        ) {
            button_salvar_cartao.visibility = View.VISIBLE
        } else {
            button_salvar_cartao.visibility = View.GONE
        }
    }

    companion object {
        fun buscaIntent(contexto: Context): Intent {
            return Intent(contexto, CadastroCartaoActivity::class.java)
        }
    }

}

/* Para teste:
println("Valor dos campos nesse instante:")
println(edit_numero_cartao.text.toString())
println(edit_nome_titular.text.toString())
println(edit_vencimento.text.toString())
println(edit_cvv.text.toString())
*/
