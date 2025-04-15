package com.practicum.playlistmaker.medialibrary.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.medialibrary.data.db.entity.FavouriteTrackEntity

@Dao
interface FavouriteTrackDao {

    @Insert(entity = FavouriteTrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavourite(favouriteTrackEntity: FavouriteTrackEntity)

    @Delete(entity = FavouriteTrackEntity::class)
    suspend fun deleteFromFavourite(favouriteTrackEntity: FavouriteTrackEntity)

    @Query("SELECT * FROM favourite_track_table")
    suspend fun getFavouriteTrackList(): List<FavouriteTrackEntity>

    @Query("SELECT trackId FROM favourite_track_table")
    suspend fun getFavouriteTrackIdList(): List<Long>

    @Query("SELECT * FROM favourite_track_table WHERE trackId = :trackId")
    suspend fun getFavourite(trackId: Long): FavouriteTrackEntity
}