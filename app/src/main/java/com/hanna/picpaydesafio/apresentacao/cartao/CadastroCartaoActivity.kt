package com.hanna.picpaydesafio.apresentacao.cartao

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.hanna.picpaydesafio.R

class CadastroCartaoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_cartao)

        // Para mostrar botão de voltar
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        //button_salvar_cartao.visibility = View.VISIBLE

    }


    private fun verificaCampos() {
        /*
        val numeroCartao = edit_numero_cartao.text.toString()
        val nomeTitular = edit_nome_titular.text.toString()
        val vencimento = edit_vencimento.text.toString()
        val cvv = edit_cvv.text.toString()
        */
    }
}
