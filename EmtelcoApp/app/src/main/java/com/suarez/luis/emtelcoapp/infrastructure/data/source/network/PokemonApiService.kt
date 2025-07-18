package com.suarez.luis.emtelcoapp.infrastructure.data.source.network

import com.suarez.luis.emtelcoapp.domain.models.PokemonResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonApiService {
    @GET("pokemon")
    suspend fun getPokemons(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): PokemonResponse
}