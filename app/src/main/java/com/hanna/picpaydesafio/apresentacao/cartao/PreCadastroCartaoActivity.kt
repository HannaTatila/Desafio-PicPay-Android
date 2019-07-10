package com.hanna.picpaydesafio.apresentacao.cartao

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hanna.picpaydesafio.R

class PreCadastroCartaoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_precadastro_cartao)

    }

    companion object {
        private const val EXTRA_NOME = "EXTRA_NOME"
        private const val EXTRA_IMAGEM = "EXTRA_IMAGEM"
        private const val EXTRA_USERNAME = "EXTRA_USERNAME"

        fun buscaIntent(
                contexto: Context,
                nome: String,
                imagem: String,
                username: String
        ): Intent {
            return Intent(contexto, PreCadastroCartaoActivity::class.java).apply {
                putExtra(EXTRA_NOME, nome)
                putExtra(EXTRA_IMAGEM, imagem)
                putExtra(EXTRA_USERNAME, username)
            }
        }
    }
}