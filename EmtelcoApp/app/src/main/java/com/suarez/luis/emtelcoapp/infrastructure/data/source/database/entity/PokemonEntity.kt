package com.suarez.luis.emtelcoapp.infrastructure.data.source.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.suarez.luis.emtelcoapp.domain.models.PokemonResult

@Entity(tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val imageUrl: String
)

// Mapper para convertir desde la API
fun PokemonResult.toEntity(): PokemonEntity {
    val pokemonId = url.trimEnd('/').split("/").last().toInt()
    return PokemonEntity(
        id = pokemonId,
        name = name,
        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$pokemonId.png"
    )
}

// Mapper para convertir desde Room a UI
fun PokemonEntity.toResult(): PokemonResult {
    return PokemonResult(
        name = name,
        url = "https://pokeapi.co/api/v2/pokemon/$id/"
    )
}