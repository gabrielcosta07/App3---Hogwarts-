package br.ufpr.grimorio

import com.google.gson.annotations.SerializedName

data class PersonagemResponse(
    @SerializedName("data") val personagens: List<Personagem>
)

data class Personagem(
    @SerializedName("name") val nome: String,
    @SerializedName("species") val especie: String?,
    @SerializedName("house") val casa: String?,
    @SerializedName("image") val imagem: String?
)
