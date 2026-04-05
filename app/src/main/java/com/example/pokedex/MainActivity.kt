package com.example.pokedex

import android.content.Intent
import android.os.Bundle
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
        
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        setupUI()
        setupObservers()
        
        viewModel.fetchPokemonList()
    }
    // Configura o RecyclerView com um adapter
    private fun setupRecyclerView() {
        binding.listPokemons.setHasFixedSize(true)
    }
// Configura os cliques dos botões de "Próximo" e "Anterior"
    private fun setupUI() {
        binding.btnGoToTeam.setOnClickListener {
            val intent = Intent(this, TeamActivity::class.java)
            startActivity(intent)
        }

        binding.btnNext.setOnClickListener {
            viewModel.nextPage()
        }

        binding.btnPrevious.setOnClickListener {
            viewModel.previousPage()
        }
//    Campo de busca desabilitado - funcionalidade será completada na Etapa 2
        binding.editSearch.isEnabled = false
        binding.editSearch.hint = getString(R.string.hint_buscar)
    }
// Configura os observadores do ViewModel
    private fun setupObservers() {
        // Observe o LiveData do ViewModel para ir a tela de detalhes do Pokémon
        viewModel.pokemonList.observe(this) { listaDePokemons ->
            val adapter = PokemonAdapter(listaDePokemons) { pokemon ->
                // Lógica de clique simplificada e moderna
                val intent = Intent(this, PokemonDetailActivity::class.java).apply {
                    putExtra("POKEMON_NAME", pokemon.name)
                    putExtra("POKEMON_ID", pokemon.url.trimEnd('/').split('/').last())
                }
                startActivity(intent)
            }
            binding.listPokemons.adapter = adapter // Atualiza o adapter
            binding.listPokemons.scrollToPosition(0)// Scroll para a posição 0
        }
        // Observe o LiveData do ViewModel para atualizar a página atual e o total de páginas
        viewModel.currentPage.observe(this) { page ->
            updatePageInfo()
        }
    // Observe o LiveData do ViewModel para atualizar a página atual e o total de páginas
        viewModel.totalPages.observe(this) { total ->
            updatePageInfo()
        }
    }
// Atualiza a página atual e o total de páginas na interface do usuário
    private fun updatePageInfo() {
        val currentPage = viewModel.currentPage.value ?: 1
        val totalPages = viewModel.totalPages.value ?: 1
        
        binding.txtPageNumber.text = getString(R.string.label_pagina, currentPage, totalPages)
        binding.btnPrevious.isEnabled = currentPage > 1
        binding.btnNext.isEnabled = currentPage < totalPages
    }
}