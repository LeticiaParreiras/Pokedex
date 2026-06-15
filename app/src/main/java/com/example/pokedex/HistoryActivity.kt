package com.example.pokedex

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokedex.data.HistoryPreferences
import com.example.pokedex.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var historyPrefs: HistoryPreferences
    private val adapter = HistoryAdapter { item ->
        // Ao clicar no item do histórico, abre a tela de detalhes
        val intent = Intent(this, PokemonDetailActivity::class.java).apply {
            putExtra("POKEMON_NAME", item.name)
            putExtra("POKEMON_ID", item.id)
        }
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        historyPrefs = HistoryPreferences(this)

        setupRecyclerView()
        setupUI()
        loadHistory()
    }

    private fun setupRecyclerView() {
        binding.listHistory.layoutManager = LinearLayoutManager(this)
        binding.listHistory.adapter = adapter
    }

    private fun setupUI() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun loadHistory() {
        val history = historyPrefs.getHistory()
        adapter.submitList(history)

        if (history.isEmpty()) {
            binding.txtHistoryMessage.visibility = View.VISIBLE
            binding.listHistory.visibility = View.GONE
        } else {
            binding.txtHistoryMessage.visibility = View.GONE
            binding.listHistory.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        // Recarrega o histórico caso tenha visitado um novo pokemon e voltado
        loadHistory()
    }
}
