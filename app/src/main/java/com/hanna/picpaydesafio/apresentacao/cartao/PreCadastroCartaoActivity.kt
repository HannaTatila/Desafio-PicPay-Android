package com.hanna.picpaydesafio.apresentacao.cartao

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.hanna.picpaydesafio.R
import com.hanna.picpaydesafio.apresentacao.base.BaseActivity
import kotlinx.android.synthetic.main.activity_precadastro_cartao.*
import kotlinx.android.synthetic.main.include_toolbar.*

class PreCadastroCartaoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_precadastro_cartao)
        configuraToolBar(toolbar_geral)

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