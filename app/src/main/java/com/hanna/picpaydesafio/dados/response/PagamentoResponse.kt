package com.hanna.picpaydesafio.dados.response

import com.google.gson.annotations.SerializedName

data class PagamentoResponse(
    @SerializedName("card_number") val numeroCartao: String,
    @SerializedName("cvv") val cvv: String,
    @SerializedName("value") val valor: Double, //TODO: trocar para tipo BigDecimal
    @SerializedName("expiry_date") val vencimento: String,
    @SerializedName("destination_user_id") val idRecebedor: Int
)