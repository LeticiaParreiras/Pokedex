package com.example.pokedex.modelView

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.model.PokemonList
import com.example.pokedex.model.listPokemonModel
import kotlinx.coroutines.launch

class pokemonListModelView: ViewModel() {
    private val pokemonListModel = listPokemonModel()
    private val _pokemonList = MutableLiveData<List<PokemonList>>()
    val pokemonList = _pokemonList

    fun fetchPokemonList() {
        viewModelScope.launch {
            try {
                val result = pokemonListModel.getPokemonList()
                _pokemonList.value = result

            } catch (e: Exception) {
            }
        }
    }
}