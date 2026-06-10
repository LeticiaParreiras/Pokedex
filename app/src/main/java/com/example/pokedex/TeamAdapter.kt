package com.example.pokedex

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.pokedex.data.Equipe
import com.example.pokedex.databinding.ItemPokemonTeamBinding

/**
 * Adapter da equipe. [onRemoveClick] é chamado ao clicar em remover.
 */
class TeamAdapter(private val onRemoveClick: (Equipe) -> Unit) :
    ListAdapter<Equipe, TeamAdapter.TeamViewHolder>(DiffCallback) {

    /** ViewHolder de um item da equipe. */
    class TeamViewHolder(private val binding: ItemPokemonTeamBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /** Preenche as views com os dados do [equipe]. */
        fun bind(equipe: Equipe, onRemoveClick: (Equipe) -> Unit) {
            binding.txtPokemonName.text = equipe.nomePokemon

            // Sprite oficial da PokeAPI
            val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${equipe.idPokemon}.png"
            binding.imgPokemon.load(imageUrl) {
                placeholder(android.R.drawable.ic_menu_report_image)
                error(android.R.drawable.ic_menu_close_clear_cancel)
            }

            binding.btnRemove.setOnClickListener { onRemoveClick(equipe) }
        }
    }

    /** Infla o layout e cria o ViewHolder. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        val binding = ItemPokemonTeamBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TeamViewHolder(binding)
    }

    /** Preenche o ViewHolder com o item da posição. */
    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        holder.bind(getItem(position), onRemoveClick)
    }

    /**
     * Calcula diferenças entre listas para atualizar só o necessário.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Equipe>() {
        // Mesmo registro no banco?
        override fun areItemsTheSame(oldItem: Equipe, newItem: Equipe) =
            oldItem.id == newItem.id

        // Conteúdo visual mudou?
        override fun areContentsTheSame(oldItem: Equipe, newItem: Equipe) =
            oldItem.nomePokemon == newItem.nomePokemon &&
                    oldItem.idPokemon == newItem.idPokemon
    }
}