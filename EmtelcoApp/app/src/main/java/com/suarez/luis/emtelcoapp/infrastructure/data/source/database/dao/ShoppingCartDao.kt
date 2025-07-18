package com.suarez.luis.emtelcoapp.infrastructure.data.source.database.dao

import androidx.room.*
import com.suarez.luis.emtelcoapp.infrastructure.data.source.database.entity.ShoppingCartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingCartDao {

    @Query("SELECT * FROM shopping_cart ORDER BY name ASC")
    fun getCartItems(): Flow<List<ShoppingCartEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(item: ShoppingCartEntity)

    @Query("DELETE FROM shopping_cart WHERE id = :pokemonId")
    suspend fun removeFromCart(pokemonId: Int)

    @Query("DELETE FROM shopping_cart")
    suspend fun clearCart()
}