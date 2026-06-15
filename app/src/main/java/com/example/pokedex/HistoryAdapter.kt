package com.example.pokedex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.pokedex.data.HistoryItem
import com.example.pokedex.databinding.ItemPokemonTeamBinding

/**
 * Adapter para o histórico, reutilizando o layout e estilo da equipe.
 */
class HistoryAdapter(private val onItemClick: (HistoryItem) -> Unit) :
    ListAdapter<HistoryItem, HistoryAdapter.HistoryViewHolder>(DiffCallback) {

    class HistoryViewHolder(private val binding: ItemPokemonTeamBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: HistoryItem, onItemClick: (HistoryItem) -> Unit) {
            binding.txtPokemonName.text = item.name

            val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${item.id}.png"
            binding.imgPokemon.load(imageUrl) {
                placeholder(android.R.drawable.ic_menu_report_image)
                error(android.R.drawable.ic_menu_close_clear_cancel)
            }

            // Escondemos o botão de remover que existe no layout da equipe
            binding.btnRemove.visibility = View.GONE

            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemPokemonTeamBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClick)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<HistoryItem>() {
        override fun areItemsTheSame(oldItem: HistoryItem, newItem: HistoryItem) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: HistoryItem, newItem: HistoryItem) =
            oldItem == newItem
    }
}
