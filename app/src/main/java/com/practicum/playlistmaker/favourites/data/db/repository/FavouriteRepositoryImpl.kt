package com.practicum.playlistmaker.favourites.data.db.repository

import com.practicum.playlistmaker.favourites.data.db.AppDatabase
import com.practicum.playlistmaker.favourites.data.db.mappers.FavouriteMap
import com.practicum.playlistmaker.favourites.domain.FavouriteRepository
import com.practicum.playlistmaker.favourites.domain.models.Favourite
import kotlinx.coroutines.flow.Flow

class FavouriteRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val mapper: FavouriteMap
) : FavouriteRepository {
    override suspend fun addToFavourite(item: Favourite) {
        appDatabase.favouriteTrackDao().addToFavourite(mapper.map(item))
    }

    override suspend fun deleteFromFavourite(item: Favourite) {
        TODO("Not yet implemented")
    }

    override fun getFavouriteIdList(): Flow<List<Long>> {
        TODO("Not yet implemented")
    }

    override fun getFavouriteItems(): Flow<List<Favourite>> {
        TODO("Not yet implemented")
    }
}