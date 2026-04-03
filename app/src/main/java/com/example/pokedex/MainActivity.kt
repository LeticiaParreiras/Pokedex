package com.example.pokedex

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
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

        setupUI()
        setupObservers()
        
        viewModel.fetchPokemonList()
    }

    private fun setupUI() {
        binding.btnNext.setOnClickListener {
            viewModel.nextPage()
        }

        binding.btnPrevious.setOnClickListener {
            viewModel.previousPage()
        }

        binding.btnGoToPage.setOnClickListener {
            val pageStr = binding.editGoToPage.text.toString()
            if (pageStr.isNotEmpty()) {
                val page = pageStr.toInt()
                val total = viewModel.totalPages.value ?: 1
                if (page in 1..total) {
                    viewModel.goToPage(page)
                    binding.editGoToPage.text.clear()
                } else {
                    Toast.makeText(this, "Página inválida (1-$total)", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnGoToTeam.setOnClickListener {
            val intent = Intent(this, TeamActivity::class.java)
            startActivity(intent)
        }

        // Listener para clique nos itens da lista
        binding.listPokemons.setOnItemClickListener { _, _, position, _ ->
            val pokemon = viewModel.pokemonList.value?.get(position)
            pokemon?.let {
                val id = it.url.split("/").filter { s -> s.isNotEmpty() }.last()
                val intent = Intent(this, PokemonDetailActivity::class.java).apply {
                    putExtra("POKEMON_NAME", it.name.replaceFirstChar { char -> char.uppercase() })
                    putExtra("POKEMON_ID", id)
                }
                startActivity(intent)
            }
        }
    }

    private fun setupObservers() {
        viewModel.pokemonList.observe(this) { listaDePokemons ->
            val itensFormatados = listaDePokemons.map { pokemon ->
                val id = pokemon.url.split("/").filter { it.isNotEmpty() }.last()
                val idFormatado = id.padStart(3, '0')
                val nomeFormatado = pokemon.name.replaceFirstChar { it.uppercase() }
                "$idFormatado - $nomeFormatado"
            }
            
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                itensFormatados
            )
            binding.listPokemons.adapter = adapter
            binding.listPokemons.setSelection(0)
        }

        viewModel.currentPage.observe(this) { page ->
            updatePageInfo()
        }

        viewModel.totalPages.observe(this) { total ->
            updatePageInfo()
        }
    }

    private fun updatePageInfo() {
        val currentPage = viewModel.currentPage.value ?: 1
        val totalPages = viewModel.totalPages.value ?: 1
        
        binding.txtPageNumber.text = getString(R.string.label_pagina, currentPage, totalPages)
        binding.btnPrevious.isEnabled = currentPage > 1
        binding.btnNext.isEnabled = currentPage < totalPages
    }
}