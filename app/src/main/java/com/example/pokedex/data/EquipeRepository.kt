package com.example.pokedex.data

import kotlinx.coroutines.flow.Flow

class EquipeRepository(private val equipeDao: EquipeDao) {

    val todaEquipes: Flow<List<Equipe>> = equipeDao.getAll()

    suspend fun getCount(): Int {
        return equipeDao.getCount()
    }

    suspend fun inserir(equipe: Equipe) {
        equipeDao.insert(equipe)
    }

    suspend fun deletar(equipe: Equipe) {
        equipeDao.delete(equipe)
    }

    suspend fun pokemonJaAdicionado(id: Int): Equipe? {
        return equipeDao.pokemonJaAdicionado(id)
    }
}
