package com.example.pokedex.data

import android.content.Context

data class HistoryItem(val id: String, val name: String)

class HistoryPreferences(context: Context) {
    private val prefs = context.getSharedPreferences("pokedex_history", Context.MODE_PRIVATE)

    fun addVisit(id: String, name: String) {
        val history = getHistory().toMutableList()
        val newItem = HistoryItem(id, name)
        
        // Remove if exists (comparison by ID)
        history.removeAll { it.id == id }
        history.add(0, newItem)

        // Keep last 10
        val limitedHistory = history.take(10)
        
        val serialized = limitedHistory.joinToString(";") { "${it.id}|${it.name}" }
        prefs.edit().putString("history_data", serialized).apply()
    }

    fun getHistory(): List<HistoryItem> {
        val saved = prefs.getString("history_data", "") ?: ""
        if (saved.isEmpty()) return emptyList()
        
        return saved.split(";").mapNotNull {
            val parts = it.split("|")
            if (parts.size == 2) HistoryItem(parts[0], parts[1]) else null
        }
    }
}
