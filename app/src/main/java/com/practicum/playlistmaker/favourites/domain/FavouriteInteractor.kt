package com.practicum.playlistmaker.favourites.domain

import com.practicum.playlistmaker.favourites.domain.models.Favourite
import kotlinx.coroutines.flow.Flow

interface FavouriteInteractor {

    suspend fun addToFavourite(item: Favourite)
    suspend fun deleteFromFavourite(item: Favourite)

    fun getFavouriteIdList(): Flow<List<Long>>
    fun getFavouriteItems(): Flow<List<Favourite>>

}