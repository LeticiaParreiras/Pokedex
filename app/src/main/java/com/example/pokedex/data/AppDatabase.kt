package com.example.pokedex.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Equipe::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun equipeDao(): EquipeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "database-pokedex"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
