package com.hanna.picpaydesafio.dados.resposta

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class PagamentoResponse(
    @SerializedName("card_number") val numeroCartao: String,
    @SerializedName("cvv") val cvv: String,
    @SerializedName("value") val valor: BigDecimal,
    @SerializedName("expiry_date") val vencimento: String,
    @SerializedName("destination_user_id") val idRecebedor: Int
)