package com.hanna.picpaydesafio.dados

class ConstantesPersistencia private constructor() {

    object CHAVE_CONTATO {
        val ID_CONTATO = "id"
        val IMG_CONTATO = "urlImagem"
        val USERNAME_CONTATO = "username"
    }

    object CHAVE_CARTAO {
        val NUMERO_CARTAO = "numeroCartao"
        val NOME_TITULAR = "nomeTitular"
        val VENCIMENTO = "vencimento"
        val CVV = "cvv"
    }

}