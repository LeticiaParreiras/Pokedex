package com.example.pokedex


import com.example.pokedex.modelView.EquipeViewModel
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.example.pokedex.data.AppDatabase
import com.example.pokedex.data.Equipe
import com.example.pokedex.data.EquipeRepository
import com.example.pokedex.data.HistoryPreferences
import com.example.pokedex.databinding.ActivityPokemonDetailBinding
import kotlinx.coroutines.launch

class PokemonDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPokemonDetailBinding
    private val viewModel: EquipeViewModel by viewModels {
        val dao = AppDatabase.getDatabase(applicationContext).equipeDao()
        EquipeViewModel.Factory(EquipeRepository(dao))
    }
    private lateinit var pokemonName: String
    private lateinit var pokemonId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

         pokemonName = intent.getStringExtra("POKEMON_NAME") ?: "Unknown"
         pokemonId = intent.getStringExtra("POKEMON_ID") ?: "1"

        // Salva no histórico (ID e Nome)
        val historyPrefs = HistoryPreferences(this)
        historyPrefs.addVisit(pokemonId, pokemonName)

        binding.txtPokemonName.text = pokemonName
        
        val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$pokemonId.png"
        
        binding.imgPokemon.load(imageUrl) {
            placeholder(android.R.drawable.ic_menu_report_image)
            error(android.R.drawable.ic_menu_close_clear_cancel)
        }

        binding.AddEquipe.setOnClickListener {
            // Criando o objeto e atribuindo os valores, já que não é uma data class
            val equipe = Equipe().apply {
                nomePokemon = pokemonName
                idPokemon = pokemonId.toInt()
            }
            viewModel.adicionarPokemon(equipe)
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        observeMessages()
    }

    private fun observeMessages() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.mensagem.collect { msg ->
                    Toast.makeText(this@PokemonDetailActivity, msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
