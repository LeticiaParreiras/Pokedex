package com.example.pokedex.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EquipeDao {
    @Query("SELECT * FROM Equipe")
    fun getAll(): Flow<List<Equipe>>

    @Insert
    suspend fun insert(equipe: Equipe)

    @Delete
    suspend fun delete(equipe: Equipe)

    @Query("SELECT COUNT(*) FROM Equipe")
    suspend fun getCount(): Int

    @Query("SELECT * FROM Equipe WHERE idPokemon = :id")
    suspend fun pokemonJaAdicionado(id: Int): Equipe?
}
