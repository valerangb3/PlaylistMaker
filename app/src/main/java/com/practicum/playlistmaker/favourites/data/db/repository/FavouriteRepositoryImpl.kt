package com.practicum.playlistmaker.favourites.data.db.repository

import com.practicum.playlistmaker.favourites.data.db.AppDatabase
import com.practicum.playlistmaker.favourites.data.db.mappers.FavouriteMap
import com.practicum.playlistmaker.favourites.domain.FavouriteRepository
import com.practicum.playlistmaker.favourites.domain.models.Favourite
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavouriteRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val mapper: FavouriteMap
) : FavouriteRepository {
    override suspend fun addToFavourite(item: Favourite) {
        appDatabase.favouriteTrackDao().addToFavourite(mapper.map(item))
    }

    override fun getFavourite(trackId: Long): Flow<Favourite> = flow {
        val favouriteTrack = appDatabase.favouriteTrackDao().getFavourite(trackId)
        emit(mapper.map(favouriteTrack))
    }

    override suspend fun deleteFromFavourite(item: Favourite) {
        appDatabase.favouriteTrackDao().deleteFromFavourite(mapper.map(item))
    }

    override fun getFavouriteIdList(): Flow<List<Long>> = flow {
        emit(appDatabase.favouriteTrackDao().getFavouriteTrackIdList())
    }

    override fun getFavouriteItems(): Flow<List<Favourite>> = flow {
        emit(
            mapper.map(
                appDatabase.favouriteTrackDao().getFavouriteTrackList()
                    .sortedWith(compareByDescending {
                        it.timestamp
                    })
            )
        )
    }


}