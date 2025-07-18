package com.suarez.luis.emtelcoapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suarez.luis.emtelcoapp.infrastructure.data.source.database.PokemonRepository
import com.suarez.luis.emtelcoapp.infrastructure.data.source.database.entity.PokemonEntity
import com.suarez.luis.emtelcoapp.infrastructure.data.source.database.entity.ShoppingCartEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    // Lista de Pokémon
    private val _pokemonList = MutableStateFlow<List<PokemonEntity>>(emptyList())
    val pokemonList: StateFlow<List<PokemonEntity>> = _pokemonList

    // Carrito
    val cartItems: StateFlow<List<ShoppingCartEntity>> =
        repository.getCartItems().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    // Total calculado del carrito
    val cartTotal: StateFlow<Int> = cartItems.map { list ->
        list.sumOf { it.price * it.quantity }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        0
    )

    private var offset = 0
    private val limit = 20

    init {
        loadMorePokemons()
    }

    fun loadMorePokemons() {
        viewModelScope.launch {
            repository.getPokemonPaginated(limit, offset).collect { data ->
                _pokemonList.value = data
            }
            offset += limit
        }
    }

    fun addPokemonToCart(pokemon: PokemonEntity) {
        viewModelScope.launch {
            repository.addToCart(pokemon)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }
}