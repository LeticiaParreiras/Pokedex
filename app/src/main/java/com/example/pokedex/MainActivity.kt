package com.example.pokedex

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pokedex.databinding.ActivityMainBinding
import com.example.pokedex.modelView.PokemonListModelView

/**
 * Tela Principal da Pokédex.
 * Responsável por exibir a lista de Pokémons, gerenciar a paginação e a navegação.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    
    // Injeção do ViewModel que gerencia os dados da lista
    private val viewModel: PokemonListModelView by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Ativa o design de tela cheia (Edge-to-Edge)
        enableEdgeToEdge()
        
        // Configuração do ViewBinding para acessar os elementos do XML
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Ajusta o padding da view principal para não ficar atrás das barras do sistema
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        setupUI()
        setupObservers()
        
        // Carrega a primeira lista de Pokémons ao iniciar
        viewModel.fetchPokemonList()
    }

    // Configurações básicas do RecyclerView
    private fun setupRecyclerView() {
        binding.listPokemons.setHasFixedSize(true)
    }

    // Configura os ouvintes de clique nos botões da interface
    private fun setupUI() {
        // Navegação para a tela de Time (Meu Time)
        binding.btnGoToTeam.setOnClickListener {
            val intent = Intent(this, TeamActivity::class.java)
            startActivity(intent)
        }

        // Navegação para a próxima página de Pokémons
        binding.btnNext.setOnClickListener {
            viewModel.nextPage()
        }

        // Navegação para a página anterior
        binding.btnPrevious.setOnClickListener {
            viewModel.previousPage()
        }
    }

    // Configura os observadores de LiveData do ViewModel
    private fun setupObservers() {
        // Observa mudanças na lista de Pokémons (quando a API responde)
        viewModel.pokemonList.observe(this) { listaDePokemons ->
            // Cria o adapter passando a lista e a função de clique
            val adapter = PokemonAdapter(listaDePokemons) { pokemon ->
                // Ao clicar em um Pokémon, abre a tela de detalhes via Intent
                val intent = Intent(this, PokemonDetailActivity::class.java).apply {
                    putExtra("POKEMON_NAME", pokemon.name.replaceFirstChar { it.uppercase() })
                    // Extrai o ID da URL para buscar a imagem na outra tela
                    putExtra("POKEMON_ID", pokemon.url.trimEnd('/').split('/').last())
                }
                startActivity(intent)
            }
            binding.listPokemons.adapter = adapter
            binding.listPokemons.scrollToPosition(0) // Volta para o topo ao trocar de página
        }

        // Observa a página atual e o total de páginas para atualizar o rodapé
        viewModel.currentPage.observe(this) { updatePageInfo() }
        viewModel.totalPages.observe(this) { updatePageInfo() }
    }

    // Atualiza o texto da paginação e habilita/desabilita os botões de navegação
    private fun updatePageInfo() {
        val currentPage = viewModel.currentPage.value ?: 1
        val totalPages = viewModel.totalPages.value ?: 1
        
        binding.txtPageNumber.text = getString(R.string.label_pagina, currentPage, totalPages)
        
        // Regras de negócio da paginação:
        binding.btnPrevious.isEnabled = currentPage > 1
        binding.btnNext.isEnabled = currentPage < totalPages
    }
}