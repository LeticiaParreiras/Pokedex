package com.example.pokedex

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pokedex.databinding.ActivityMapBinding

class MapActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        enableEdgeToEdge()
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupUI()
    }

    private fun setupUI() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        // Configuração do WebView para mostrar o mapa
        binding.webViewMap.apply {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
            // Carrega o Google Maps com a busca por "Pokemon"
            loadUrl("https://www.google.com/maps/search/Pokemon")
        }
    }
}