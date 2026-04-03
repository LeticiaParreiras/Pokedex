package com.example.pokedex

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pokedex.databinding.ActivityMainBinding
import com.example.pokedex.modelView.pokemonListModelView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: pokemonListModelView by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupObservers()
        viewModel.fetchPokemonList()
    }
    private fun setupObservers() {
        // Observando o LiveData público que criamos no ViewModel
        viewModel.pokemonList.observe(this) { listaDePokemons ->

            // A API devolve objetos completos, mas o ArrayAdapter simples só quer textos.
            // Vamos criar uma lista contendo apenas os nomes dos Pokémon:
            val nomesDosPokemons = listaDePokemons.map { it.name.replaceFirstChar { char -> char.uppercase() } }

            // Configuramos o adapter com a nossa lista de nomes
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                nomesDosPokemons
            )

            // Colocamos o adapter na ListView do seu layout
            binding.listPokemons.adapter = adapter
        }
    }
    }
