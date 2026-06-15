package com.example.pokedex.data

import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class EquipeRepositoryTest {
    // Mock do DAO - substitui o banco de dados real nos testes
    private lateinit var equipeDao: EquipeDao
    private lateinit var repository: EquipeRepository

    @Before
    fun setUp() {
        equipeDao = mock()
        repository = EquipeRepository(equipeDao)
    }

    @Test
    fun `todaEquipes deve expor o flow retornado pelo DAO`() = runTest {
        val equipe = Equipe().apply { id = 1; nomePokemon = "Pikachu"; idPokemon = 25 }
        whenever(equipeDao.getAll()).thenReturn(flowOf(listOf(equipe)))

        //Recria repository para capturar o flow configurado no setUp
        val repo = EquipeRepository(equipeDao)

        repo.todaEquipes.collect { lista ->
            assertEquals(1, lista.size)
            assertEquals("Pikachu", lista[0].nomePokemon)
        }
    }

    @Test
    fun `getCount deve retornar o valor fornecido pelo DAO` () = runTest {
        whenever(equipeDao.getCount()).thenReturn(3)
        val resultado = repository.getCount()
        assertEquals(3, resultado)
    }

    @Test
    fun `getCount deve retornar zero quando equipe está vazia` () = runTest {
        whenever(equipeDao.getCount()).thenReturn(0)
        val resultado = repository.getCount()
        assertEquals(0, resultado)
    }

    @Test
    fun `inserir deve delegar a chamada ao DAO` () = runTest {
        val equipe = Equipe().apply { nomePokemon = "Bulbasaur"; idPokemon = 1 }
        repository.inserir(equipe)
        verify(equipeDao).insert(equipe)
    }

    @Test
    fun `deletar deve delar a chamada ao DAO` () = runTest {
        val equipe = Equipe().apply { id = 2; nomePokemon = "Charmander"; idPokemon = 4 }
        repository.deletar(equipe)
        verify(equipeDao).delete(equipe)
    }

    @Test
    fun `pokemonJaAdicionado deve retornar a equipe quando pokemon existe`() = runTest {
        val equipe = Equipe().apply { id = 1; nomePokemon = "Squirtle"; idPokemon = 7 }
        whenever(equipeDao.pokemonJaAdicionado(7)).thenReturn(equipe)

        val resultado = repository.pokemonJaAdicionado(7)

        assertNotNull(resultado)
        assertEquals("Squirtle", resultado?.nomePokemon)
    }

    @Test
    fun `pokemonJaAdicionado deve retornar null quando pokemon nao existe`() = runTest {
        whenever(equipeDao.pokemonJaAdicionado(999)).thenReturn(null)

        val resultado = repository.pokemonJaAdicionado(999)

        assertNull(resultado)
    }
}