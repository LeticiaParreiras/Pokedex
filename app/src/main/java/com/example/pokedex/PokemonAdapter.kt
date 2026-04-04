package com.example.pokedex

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.pokedex.databinding.ItemPokemonBinding
import com.example.pokedex.model.PokemonList
// Classe PokemonAdapter Serve para transformar a lista de pokemons da API em um recyclerView.
class PokemonAdapter(
    private val pokemons: List<PokemonList>, // Lista de Pokémons
    private val onItemClick: (PokemonList) -> Unit // Função de callback para lidar com cliques
) : RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemPokemonBinding) : RecyclerView.ViewHolder(binding.root)
    //Metodo onCreateViewHolder é chamado quando o RecyclerView precisa de um novo ViewHolder para representar um item.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPokemonBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pokemon = pokemons[position]
        holder.binding.txtName.text = pokemon.name

        val id = pokemon.url.trimEnd('/').split('/').last() // Extrai o ID do Pokémon da URL
        val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"// URL dos sprites da PokeAPI

        // Enquanto a imagem está sendo carregada, exibe um indicador de progresso
        holder.binding.imgPokemon.load(imageUrl) {
            crossfade(true)
            placeholder(android.R.drawable.progress_indeterminate_horizontal)
        }
    // Configura um clique de item para chamar a função onItemClick
        holder.itemView.setOnClickListener {
            onItemClick(pokemon)
        }
    }

    override fun getItemCount() = pokemons.size
}