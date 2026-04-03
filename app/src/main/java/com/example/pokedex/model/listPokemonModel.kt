package com.example.pokedex.model


import com.example.pokedex.PokemonApi


class listPokemonModel {
    suspend fun getPokemonList(): List<PokemonList> {
            return try{
                val pokemonList = PokemonApi.service.getPokemonList()
                return pokemonList.results
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
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