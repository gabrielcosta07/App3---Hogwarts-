package br.ufpr.grimorio

import retrofit2.http.GET
import retrofit2.http.Query

interface HogwartsApi {
    @GET("characters")
    suspend fun buscarPersonagens(@Query("search") nome: String): PersonagemResponse

    @GET("creatures")
    suspend fun buscarCriaturas(@Query("search") nome: String): CriaturaResponse

    companion object {
        const val BASE_URL = "https://hogwarts-api.com/api/"
    }
}
