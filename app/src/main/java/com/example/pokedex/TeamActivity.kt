package com.example.pokedex

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.data.AppDatabase
import com.example.pokedex.data.EquipeRepository
import com.example.pokedex.modelView.EquipeViewModel
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.launch

class TeamActivity : AppCompatActivity() {
    private lateinit var toolbar: MaterialToolbar
    private lateinit var txtTeamMessage: TextView
    private lateinit var listTeam: RecyclerView

    private val viewModel: EquipeViewModel by viewModels {
        val dao = AppDatabase.getDatabase(applicationContext).equipeDao()
        EquipeViewModel.Factory(EquipeRepository(dao))
    }

    private val adapter = TeamAdapter { equipe ->
        viewModel.removerPokemon(equipe)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team)

        toolbar = findViewById(R.id.toolbar)
        txtTeamMessage = findViewById(R.id.txtTeamMessage)
        listTeam = findViewById(R.id.listTeam)

        setupRecyclerView()
        setupWindowInsets()
        observeViewModel()

        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        listTeam.layoutManager = LinearLayoutManager(this)
        listTeam.adapter = adapter
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.equipes.collect { lista ->
                    adapter.submitList(lista)
                    if (lista.isEmpty()) {
                        txtTeamMessage.visibility = View.VISIBLE
                        listTeam.visibility = View.GONE
                    } else {
                        txtTeamMessage.visibility = View.GONE
                        listTeam.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}
