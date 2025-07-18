package com.suarez.luis.emtelcoapp.infrastructure.data.source.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.suarez.luis.emtelcoapp.infrastructure.data.source.database.dao.PokemonDao
import com.suarez.luis.emtelcoapp.infrastructure.data.source.database.dao.ShoppingCartDao
import com.suarez.luis.emtelcoapp.infrastructure.data.source.database.entity.PokemonEntity
import com.suarez.luis.emtelcoapp.infrastructure.data.source.database.entity.ShoppingCartEntity

@Database(
    entities = [PokemonEntity::class, ShoppingCartEntity::class],
    version = 2,
    exportSchema = false
)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
    abstract fun shoppingCartDao(): ShoppingCartDao
}