package com.example.pokedex.modelView

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.pokedex.data.Equipe
import com.example.pokedex.data.EquipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class EquipeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var repository: EquipeRepository
    private lateinit var viewModel: EquipeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mock()
        whenever(repository.todaEquipes).thenReturn(flowOf(emptyList()))
        viewModel = EquipeViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `adicionarPokemon deve emitir mensagem quando pokemon ja foi adicionado`() = runTest {
        val equipe = Equipe().apply { nomePokemon = "Pikachu"; idPokemon = 25 }
        whenever(repository.pokemonJaAdicionado(25)).thenReturn(equipe)

        viewModel.mensagem.test {
            viewModel.adicionarPokemon(equipe)
            advanceUntilIdle()

            assertEquals("Pokemon já adicionado!", awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `adicionarPokemon deve emitir mensagem quando equipe esta cheia`() = runTest {
        val equipe = Equipe().apply { nomePokemon = "Eevee"; idPokemon = 133 }
        whenever(repository.pokemonJaAdicionado(133)).thenReturn(null)
        whenever(repository.getCount()).thenReturn(6)

        viewModel.mensagem.test {
            viewModel.adicionarPokemon(equipe)
            advanceUntilIdle()

            assertEquals("Equipe cheia!", awaitItem())
            verify(repository, never()).inserir(any())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `adicionarPokemon deve inserir e emitir sucesso quando ha vagas`() = runTest {
        val equipe = Equipe().apply { nomePokemon = "Gengar"; idPokemon = 94 }
        whenever(repository.pokemonJaAdicionado(94)).thenReturn(null)
        whenever(repository.getCount()).thenReturn(2)

        viewModel.mensagem.test {
            viewModel.adicionarPokemon(equipe)
            advanceUntilIdle()

            verify(repository).inserir(equipe)
            assertEquals("Pokemon adicionado!", awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `adicionarPokemon deve inserir quando equipe tem exatamente 5 pokemon`() = runTest {
        val equipe = Equipe().apply { nomePokemon = "Snorlax"; idPokemon = 143 }
        whenever(repository.pokemonJaAdicionado(143)).thenReturn(null)
        whenever(repository.getCount()).thenReturn(5)

        viewModel.mensagem.test {
            viewModel.adicionarPokemon(equipe)
            advanceUntilIdle()

            verify(repository).inserir(equipe)
            assertEquals("Pokemon adicionado!", awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `removerPokemon deve chamar deletar no repositorio`() = runTest {
        val equipe = Equipe().apply { id = 3; nomePokemon = "Mewtwo"; idPokemon = 150 }

        viewModel.removerPokemon(equipe)
        advanceUntilIdle()

        verify(repository).deletar(equipe)
    }

    @Test
    fun `Factory deve criar EquipeViewModel corretamente`() {
        val factory = EquipeViewModel.Factory(repository)
        val vm = factory.create(EquipeViewModel::class.java)
        assertNotNull(vm)
        assertTrue(vm is EquipeViewModel)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Factory deve lancar excecao para classe desconhecida`() {
        val factory = EquipeViewModel.Factory(repository)
        factory.create(FakeViewModel::class.java)
    }

    private class FakeViewModel : androidx.lifecycle.ViewModel()
}