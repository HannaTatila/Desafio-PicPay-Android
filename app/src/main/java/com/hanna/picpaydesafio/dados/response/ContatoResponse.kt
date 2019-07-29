package com.hanna.picpaydesafio.dados.response

import com.google.gson.annotations.SerializedName

data class ContatoResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val nome: String,
    @SerializedName("img") val imagem: String,
    @SerializedName("username") val username: String
)