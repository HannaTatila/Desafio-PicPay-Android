package com.hanna.picpaydesafio.dados.response

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class CorpoTransacaoResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("value") val valor: BigDecimal,
    @SerializedName("destination_user") val recebedor: ContatoResponse,
    @SerializedName("success") val sucesso: Boolean,
    @SerializedName("status") val estado: String
)

