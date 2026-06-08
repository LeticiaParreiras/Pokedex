package com.example.pokedex

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.view.animation.CycleInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.pokedex.databinding.ActivityRandomPokemonBinding
import com.example.pokedex.model.listPokemonModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.sqrt
import kotlin.random.Random

/**
 * Tela de Pokémon Aleatório.
 * O usuário deve sacudir o celular para "abrir" uma pokebola e revelar um Pokémon sorteado.
 */
class RandomPokemonActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var binding: ActivityRandomPokemonBinding
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f
    
    // Sensibilidade do chacoalhão
    private val SHAKE_THRESHOLD = 18f
    private var shakeCount = 0
    private val SHAKES_REQUIRED = 12 // Quantidade de chacoalhadas necessárias
    private var isRevealed = false
    private var isAnimating = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Ativa o design Edge-to-Edge
        enableEdgeToEdge()
        
        binding = ActivityRandomPokemonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ajusta os paddings para as barras do sistema
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupSensors()
        setupUI()
    }

    private fun setupSensors() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH
        acceleration = 0f
    }

    private fun setupUI() {
        // Configura o botão de voltar na toolbar
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        // Botão para resetar e tentar de novo
        binding.btnTryAgain.setOnClickListener {
            resetUI()
        }
    }

    private fun resetUI() {
        isRevealed = false
        shakeCount = 0
        binding.resultContainer.visibility = View.GONE
        binding.imgPokeball.visibility = View.VISIBLE
        binding.txtInstructions.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        // Registra o ouvinte do acelerômetro
        accelerometer?.also { acc ->
            sensorManager.registerListener(this, acc, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        // Remove o ouvinte para economizar bateria
        sensorManager.unregisterListener(this)
    }

    // Chamado quando há mudança nos sensores
    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null || isRevealed) return

        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        // Cálculo da aceleração ignorando a gravidade
        lastAcceleration = currentAcceleration
        currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
        val delta = currentAcceleration - lastAcceleration
        acceleration = acceleration * 0.9f + delta

        // Se a aceleração ultrapassar o limite, incrementa o contador e treme
        if (acceleration > SHAKE_THRESHOLD) {
            startShakeAnimation()
            shakeCount++
            
            if (shakeCount >= SHAKES_REQUIRED) {
                revealRandomPokemon()
            }
        }
    }

    private fun startShakeAnimation() {
        if (isAnimating) return
        isAnimating = true

        val rotate = PropertyValuesHolder.ofFloat(View.ROTATION, -15f, 15f)
        val translate = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, -15f, 15f)

        ObjectAnimator.ofPropertyValuesHolder(binding.imgPokeball, rotate, translate).apply {
            duration = 80
            interpolator = CycleInterpolator(3f)
            start()
        }

        lifecycleScope.launch {
            delay(120)
            isAnimating = false
        }
    }

    private fun revealRandomPokemon() {
        isRevealed = true
        
        lifecycleScope.launch {
            // Sorteia um ID entre 1 e 1025
            val randomId = Random.nextInt(1, 1026) 
            val pokemonModel = listPokemonModel()
            
            // Busca o Pokémon na PokeAPI usando offset para pegar apenas um resultado aleatório
            val response = pokemonModel.getPokemonList(offset = randomId - 1, limit = 1)
            
            val pokemon = response?.results?.firstOrNull()
            if (pokemon != null) {
                val name = pokemon.name.replaceFirstChar { it.uppercase() }
                val id = pokemon.url.trimEnd('/').split('/').last()
                val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"

                // Animação de abertura antes de mostrar
                delay(300)

                // Esconde a pokébola e as instruções, mostra o resultado
                binding.imgPokeball.visibility = View.GONE
                binding.txtInstructions.visibility = View.GONE
                
                binding.txtRevealedName.text = name
                binding.imgRevealedPokemon.load(imageUrl) {
                    crossfade(true)
                    placeholder(android.R.drawable.ic_menu_report_image)
                }
                binding.resultContainer.visibility = View.VISIBLE
            } else {
                // Em caso de erro, permite tentar de novo
                isRevealed = false
                shakeCount = 0
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Não necessário para esta implementação
    }
}