package com.suarez.luis.emtelcoapp.infrastructure.di

import android.content.Context
import androidx.room.Room
import com.suarez.luis.emtelcoapp.infrastructure.data.source.database.PokemonDatabase
import com.suarez.luis.emtelcoapp.infrastructure.data.source.database.PokemonRepository
import com.suarez.luis.emtelcoapp.infrastructure.data.source.database.dao.PokemonDao
import com.suarez.luis.emtelcoapp.infrastructure.data.source.database.dao.ShoppingCartDao
import com.suarez.luis.emtelcoapp.infrastructure.data.source.network.PokemonApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun providePokemonApi(retrofit: Retrofit): PokemonApiService =
        retrofit.create(PokemonApiService::class.java)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PokemonDatabase =
        Room.databaseBuilder(
            context,
            PokemonDatabase::class.java,
            "pokemon_db"
        ).build()

    @Provides
    @Singleton
    fun providePokemonDao(db: PokemonDatabase): PokemonDao = db.pokemonDao()

    @Provides
    @Singleton
    fun provideShoppingCartDao(db: PokemonDatabase): ShoppingCartDao = db.shoppingCartDao()

    @Provides
    @Singleton
    fun provideRepository(api: PokemonApiService, dao: PokemonDao, cartDao: ShoppingCartDao): PokemonRepository =
        PokemonRepository(api, dao, cartDao)
}