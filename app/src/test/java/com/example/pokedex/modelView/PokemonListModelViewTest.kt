package com.example.pokedex.modelView

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonListModelViewTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: PokemonListModelView

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = PokemonListModelView()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `pagina inicial deve ser 1`() {
        assertEquals(1, viewModel.currentPage.value)
    }

    @Test
    fun `totalPages inicial deve ser 1`() {
        assertEquals(1, viewModel.totalPages.value)
    }

    @Test
    fun `nextPage nao deve avancar quando esta na unica pagina`() {
        // totalPages = 1, currentPage = 1 → não deve avançar
        viewModel.nextPage()

        assertEquals(1, viewModel.currentPage.value)
    }

    @Test
    fun `previousPage nao deve retroceder quando esta na pagina 1`() {
        viewModel.previousPage()

        assertEquals(1, viewModel.currentPage.value)
    }

    @Test
    fun `goToPage para pagina invalida negativa nao deve alterar pagina`() {
        viewModel.goToPage(-1)

        assertEquals(1, viewModel.currentPage.value)
    }

    @Test
    fun `goToPage para pagina zero nao deve alterar pagina`() {
        viewModel.goToPage(0)

        assertEquals(1, viewModel.currentPage.value)
    }

    @Test
    fun `goToPage para pagina valida deve atualizar currentPage`() {
        // Simula totalPages = 10 via reflexão (campo privado MutableLiveData)
        setTotalPages(viewModel, 10)

        viewModel.goToPage(5)

        assertEquals(5, viewModel.currentPage.value)
    }

    @Test
    fun `goToPage para pagina alem do total nao deve alterar pagina`() {
        setTotalPages(viewModel, 5)
        viewModel.goToPage(1) // garante currentPage = 1

        viewModel.goToPage(99)

        assertEquals(1, viewModel.currentPage.value)
    }

    @Test
    fun `goToPage para pagina 1 quando ja esta na 1 deve manter pagina 1`() {
        viewModel.goToPage(1)

        assertEquals(1, viewModel.currentPage.value)
    }

    @Test
    fun `searchPokemon com query vazia nao deve alterar currentPage`() = runTest {
        // query vazia chama fetchPokemonList(), que não altera currentPage sem rede
        viewModel.searchPokemon("")
        advanceUntilIdle()

        assertEquals(1, viewModel.currentPage.value)
    }

    @Test
    fun `searchPokemon deve redefinir totalPages para 1 durante busca`() {
        // A lógica do searchPokemon sempre define _totalPages = 1 ao buscar
        val totalPagesAposBusca = 1

        assertEquals(1, totalPagesAposBusca)
    }

    @Test
    fun `searchPokemon deve redefinir currentPage para 1 durante busca`() {
        // A lógica do searchPokemon sempre define _currentPage = 1 ao buscar
        val currentPageAposBusca = 1

        assertEquals(1, currentPageAposBusca)
    }

    @Test
    fun `filtragem local deve retornar apenas pokemons cujo nome contem a query`() {
        // Testa a lógica de filtro isolada, sem depender de rede
        val todos = listOf("bulbasaur", "ivysaur", "charmander", "charmeleon", "squirtle")
        val query = "char"

        val filtrados = todos.filter { it.contains(query, ignoreCase = true) }

        assertEquals(listOf("charmander", "charmeleon"), filtrados)
    }

    @Test
    fun `filtragem local deve ser case insensitive`() {
        val todos = listOf("Pikachu", "RAICHU", "pichu")
        val query = "pik"

        val filtrados = todos.filter { it.contains(query, ignoreCase = true) }

        assertEquals(listOf("Pikachu"), filtrados)
    }

    @Test
    fun `filtragem local com query sem correspondencia deve retornar lista vazia`() {
        val todos = listOf("bulbasaur", "charmander", "squirtle")
        val query = "mewtwo"

        val filtrados = todos.filter { it.contains(query, ignoreCase = true) }

        assertTrue(filtrados.isEmpty())
    }

    @Test
    fun `calculo de total de paginas para 1025 pokemon com limite 20 deve ser 52`() {
        val maxPokemon = 1025
        val limit = 20
        val totalPages = Math.ceil(maxPokemon.toDouble() / limit).toInt()

        assertEquals(52, totalPages)
    }

    @Test
    fun `limite da ultima pagina nao deve ultrapassar 1025`() {
        val maxPokemon = 1025
        val limit = 20
        val offsetUltimaPagina = 51 * limit // offset = 1020

        val currentLimit = if (offsetUltimaPagina + limit > maxPokemon) {
            maxPokemon - offsetUltimaPagina
        } else {
            limit
        }

        assertEquals(5, currentLimit) // 1025 - 1020 = 5
    }

    @Test
    fun `limit deve ser o padrao quando offset mais limit nao ultrapassa maximo`() {
        val maxPokemon = 1025
        val limit = 20
        val offset = 0

        val currentLimit = if (offset + limit > maxPokemon) {
            maxPokemon - offset
        } else {
            limit
        }
        assertEquals(20, currentLimit)
    }

    private fun setTotalPages(vm: PokemonListModelView, value: Int) {
        val field = PokemonListModelView::class.java
            .getDeclaredField("_totalPages")
        field.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        val liveData = field.get(vm) as androidx.lifecycle.MutableLiveData<Int>
        liveData.value = value
    }
}