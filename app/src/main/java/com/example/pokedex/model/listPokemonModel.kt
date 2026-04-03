package com.example.pokedex.model


import com.example.pokedex.PokemonApi


class listPokemonModel {
    suspend fun getPokemonList(offset: Int = 0, limit: Int = 20): PokemonResponse? {
            return try{
                return PokemonApi.service.getPokemonList(offset = offset, limit = limit)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }


data class PokemonResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonList>
)
data class PokemonList(
    val name: String,
    val url: String
)