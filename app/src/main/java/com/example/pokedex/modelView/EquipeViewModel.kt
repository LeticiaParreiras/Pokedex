package com.example.pokedex.modelView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pokedex.data.Equipe
import com.example.pokedex.data.EquipeRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EquipeViewModel(private val repository: EquipeRepository) : ViewModel() {

    private val _mensagem = MutableSharedFlow<String>()
    val mensagem: SharedFlow<String> = _mensagem

    val equipes: StateFlow<List<Equipe>> = repository.todaEquipes
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun adicionarPokemon(equipe: Equipe) {
        viewModelScope.launch {
            val pokemonJaAdicionado = repository.pokemonJaAdicionado(equipe.idPokemon)
            if (pokemonJaAdicionado != null) {
                _mensagem.emit("Pokemon já adicionado!")
                return@launch
            }

            val currentCount = repository.getCount()
            if (currentCount >= 6) {
                _mensagem.emit("Equipe cheia!")
            } else {
                repository.inserir(equipe)
                _mensagem.emit("Pokemon adicionado!")
            }
        }
    }

    fun removerPokemon(equipe: Equipe) {
        viewModelScope.launch {
            repository.deletar(equipe)
        }
    }

    class Factory(private val repository: EquipeRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EquipeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return EquipeViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
