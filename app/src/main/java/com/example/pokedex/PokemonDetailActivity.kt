package com.example.pokedex

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil.load
import com.example.pokedex.databinding.ActivityPokemonDetailBinding

class PokemonDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPokemonDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val pokemonName = intent.getStringExtra("POKEMON_NAME") ?: "Unknown"
        val pokemonId = intent.getStringExtra("POKEMON_ID") ?: "1"

        binding.txtPokemonName.text = pokemonName
        
        // URL oficial dos sprites da PokeAPI
        val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$pokemonId.png"
        
        binding.imgPokemon.load(imageUrl) {
            placeholder(android.R.drawable.ic_menu_report_image)
            error(android.R.drawable.ic_menu_close_clear_cancel)
        }

        // Configura a Toolbar para voltar
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}