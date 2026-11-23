package br.ufpr.grimorio

import com.google.gson.annotations.SerializedName

data class CriaturaResponse(
    @SerializedName("data") val criaturas: List<Criatura>
)

data class Criatura(
    @SerializedName("name") val nome: String,
    @SerializedName("image") val imagem: String?
)
