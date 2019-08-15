package com.hanna.picpaydesafio.apresentacao.cartao

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hanna.picpaydesafio.R
import kotlinx.android.synthetic.main.activity_precadastro_cartao.*
import kotlinx.android.synthetic.main.include_botao_voltar.*

class PreCadastroCartaoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_precadastro_cartao)

        controlaEventoClique()
    }

    private fun controlaEventoClique() {
        ib_botaoVoltar.setOnClickListener { onBackPressed() }
        btn_cadastrarCartao.setOnClickListener { chamaTelaCadastro() }
    }

    private fun chamaTelaCadastro() {
        val intent = CadastroCartaoActivity.buscaIntent(this, null)
        this.startActivity(intent)
    }

    companion object {
        fun buscaIntent(contexto: Context) = Intent(contexto, PreCadastroCartaoActivity::class.java)
    }
}