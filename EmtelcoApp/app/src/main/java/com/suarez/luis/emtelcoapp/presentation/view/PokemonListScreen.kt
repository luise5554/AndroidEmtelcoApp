package com.suarez.luis.emtelcoapp.presentation.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.suarez.luis.emtelcoapp.presentation.viewmodel.PokemonViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    viewModel: PokemonViewModel = hiltViewModel(),
    onGoToCart: () -> Unit
) {
    val pokemonList by viewModel.pokemonList.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista Pokemon") },
                actions = {
                    IconButton(onClick = onGoToCart) {
                        BadgedBox(
                            badge = {
                                if (cartItems.isNotEmpty()) {
                                    Badge(
                                        modifier = Modifier
                                            .padding(top = 12.dp, end = 5.dp)
                                    ) {
                                        Text("${cartItems.size}")
                                    }
                                }
                            }
                        ) {
                            Box(Modifier.padding(4.dp)) {
                                Icon(
                                    Icons.Default.ShoppingCart,
                                    contentDescription = "Carrito",
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            itemsIndexed(pokemonList) { index, pokemon ->
                PokemonItem(
                    pokemon = pokemon,
                    onAddToCart = { viewModel.addPokemonToCart(it) }
                )

                if (index == pokemonList.lastIndex) {
                    LaunchedEffect(Unit) {
                        viewModel.loadMorePokemons()
                    }
                }
            }
        }
    }
}