package com.suarez.luis.emtelcoapp.infrastructure.data.source.database

import com.suarez.luis.emtelcoapp.infrastructure.data.source.database.dao.PokemonDao
import com.suarez.luis.emtelcoapp.infrastructure.data.source.database.dao.ShoppingCartDao
import com.suarez.luis.emtelcoapp.infrastructure.data.source.database.entity.PokemonEntity
import com.suarez.luis.emtelcoapp.infrastructure.data.source.database.entity.ShoppingCartEntity
import com.suarez.luis.emtelcoapp.infrastructure.data.source.database.entity.toEntity
import com.suarez.luis.emtelcoapp.infrastructure.data.source.network.PokemonApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.io.IOException

class PokemonRepository(
    private val api: PokemonApiService,
    private val dao: PokemonDao,
    private val cartDao: ShoppingCartDao
) {

    fun getPokemonPaginated(limit: Int, offset: Int): Flow<List<PokemonEntity>> = flow {
        try {
            // Primero emitir lo que hay en Room
            val localData = dao.getAllPokemon().first()
            emit(localData)

            // Intentar actualizar desde API
            val response = api.getPokemons(limit, offset)
            val newEntities = response.results.map { it.toEntity() }

            // Guardar/actualizar en Room
            dao.insertPokemonList(newEntities)

            // Emitir la lista actualizada
            val updatedData = dao.getAllPokemon().first()
            emit(updatedData)

        } catch (e: IOException) {
            // Si no hay internet, emitimos lo local
            val localData = dao.getAllPokemon().first()
            emit(localData)
        }
    }

    fun randomPrice(): Int {
        return (10_000..100_000).random()
    }

    suspend fun addToCart(pokemon: PokemonEntity) {
        val cartItem = ShoppingCartEntity(
            id = pokemon.id,
            name = pokemon.name,
            imageUrl = pokemon.imageUrl,
            quantity = 1,
            price = randomPrice()
        )
        cartDao.insertOrUpdate(cartItem)
    }

    suspend fun clearCart(){
        cartDao.clearCart()
    }

    fun getCartItems(): Flow<List<ShoppingCartEntity>> = cartDao.getCartItems()
}