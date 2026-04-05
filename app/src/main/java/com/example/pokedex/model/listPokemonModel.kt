package com.example.pokedex.model


import com.example.pokedex.PokemonApi

// Classe responsável por gerenciar a lógica de dados dos Pokémons (Model)
class listPokemonModel {
    // Função suspensa que busca a lista de pokémons do serviço API de forma assíncrona
    suspend fun getPokemonList(offset: Int = 0, limit: Int = 20): PokemonResponse? {
            return try{
                // Chama o serviço do Retrofit passando o deslocamento (offset) e o limite
                return PokemonApi.service.getPokemonList(offset = offset, limit = limit)
            } catch (e: Exception) {
                e.printStackTrace()
                null // Retorna null em caso de erro na rede ou na conversão
            }
        }
    }

// Data Class que representa a estrutura completa da resposta da PokeAPI
data class PokemonResponse(
    val count: Int,      // Quantidade total de pokémons existentes na API
    val next: String?,   // URL para a próxima página de resultados
    val previous: String?, // URL para a página anterior
    val results: List<PokemonList> // Lista de pokémons retornada
)

// Data Class que representa um item básico de Pokémon na lista
data class PokemonList(
    val name: String, // Nome do Pokémon
    val url: String   // URL com os detalhes do Pokémon (usada para extrair o ID)
)