package com.example.pokedex


import com.example.pokedex.model.PokemonResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// Interface que define os endpoints da PokeAPI utilizando Retrofit
interface PokemonService {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int = 20, // Define quantos pokémons buscar por vez
        @Query("offset") offset: Int = 0 // Define a partir de qual posição começar a busca (para paginação)
    ): PokemonResponse
}

object PokemonApi{
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/") // URL base da API
        .addConverterFactory(GsonConverterFactory.create()) // Converte o JSON da resposta para objetos Kotlin (GSON)
        .build()

    // Serviço que será usado para fazer as chamadas de rede
    val service: PokemonService = retrofit.create(PokemonService::class.java)
}