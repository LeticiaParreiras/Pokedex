package com.example.pokedex.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Equipe {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var nomePokemon: String = ""
    var idPokemon: Int = 0
}
