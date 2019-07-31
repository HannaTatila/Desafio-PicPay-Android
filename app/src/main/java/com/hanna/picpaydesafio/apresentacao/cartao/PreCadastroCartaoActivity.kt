package com.hanna.picpaydesafio.apresentacao.cartao

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hanna.picpaydesafio.R
import kotlinx.android.synthetic.main.activity_precadastro_cartao.*

class PreCadastroCartaoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_precadastro_cartao)

        controlaEventoClique()
    }

    private fun controlaEventoClique() {
        botao_voltar_pre_cadastro.setOnClickListener { onBackPressed() }
        button_cadastrar_cartao.setOnClickListener { chamaTelaCadastro() }
    }

    private fun chamaTelaCadastro() {
        val intent = CadastroCartaoActivity.buscaIntent(this)
        this.startActivity(intent)

    }

    companion object {
        fun buscaIntent(contexto: Context): Intent {
            return Intent(contexto, PreCadastroCartaoActivity::class.java)
        }
    }
}