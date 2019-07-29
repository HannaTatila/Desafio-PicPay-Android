package com.hanna.picpaydesafio.dados.response

import com.google.gson.annotations.SerializedName

data class TransacaoResponse(
    @SerializedName("transaction") val transacao: CorpoTransacaoResponse
)