package com.example.pokedex.modelView

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.model.PokemonList
import com.example.pokedex.model.listPokemonModel
import kotlinx.coroutines.launch

class PokemonListModelView: ViewModel() {
    private val pokemonListModel = listPokemonModel()
    
    private val _pokemonList = MutableLiveData<List<PokemonList>>()
    val pokemonList: LiveData<List<PokemonList>> = _pokemonList

    private var currentOffset = 0
    private val limit = 20
    private val maxPokemon = 1025

    private val _currentPage = MutableLiveData<Int>(1)
    val currentPage: LiveData<Int> = _currentPage

    private val _totalPages = MutableLiveData<Int>(1)
    val totalPages: LiveData<Int> = _totalPages

    fun fetchPokemonList() {
        viewModelScope.launch {
            try {
                // Ajusta o limite se estiver na última página para não ultrapassar 1025
                val currentLimit = if (currentOffset + limit > maxPokemon) {
                    maxPokemon - currentOffset
                } else {
                    limit
                }

                if (currentLimit <= 0) return@launch

                val response = pokemonListModel.getPokemonList(offset = currentOffset, limit = currentLimit)
                response?.let {
                    _pokemonList.value = it.results
                    // Limitamos o total de páginas com base no maxPokemon (1025)
                    _totalPages.value = Math.ceil(maxPokemon.toDouble() / limit).toInt()
                }
            } catch (e: Exception) {
                // Tratar erro se necessário
            }
        }
    }

    fun nextPage() {
        val total = _totalPages.value ?: 1
        val current = _currentPage.value ?: 1
        if (current < total) {
            currentOffset += limit
            _currentPage.value = current + 1
            fetchPokemonList()
        }
    }

    fun previousPage() {
        val current = _currentPage.value ?: 1
        if (current > 1) {
            currentOffset -= limit
            _currentPage.value = current - 1
            fetchPokemonList()
        }
    }

    fun goToPage(page: Int) {
        val total = _totalPages.value ?: 1
        if (page in 1..total) {
            currentOffset = (page - 1) * limit
            _currentPage.value = page
            fetchPokemonList()
        }
    }
}