package com.practicum.playlistmaker.medialibrary.domain.favourite

import com.practicum.playlistmaker.medialibrary.domain.favourite.models.Favourite
import kotlinx.coroutines.flow.Flow

interface FavouriteRepository {

    suspend fun addToFavourite(item: Favourite)
    suspend fun deleteFromFavourite(item: Favourite)

    fun getFavouriteIdList(): Flow<List<Long>>
    fun getFavouriteItems(): Flow<List<Favourite>>
    fun getFavourite(trackId: Long): Flow<Favourite>

}