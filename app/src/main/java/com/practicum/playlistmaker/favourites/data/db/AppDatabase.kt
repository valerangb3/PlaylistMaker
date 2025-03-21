package com.practicum.playlistmaker.favourites.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.favourites.data.db.dao.FavouriteTrackDao
import com.practicum.playlistmaker.favourites.data.db.entity.FavouriteTrackEntity

@Database(version = 2, entities = [FavouriteTrackEntity::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun favouriteTrackDao(): FavouriteTrackDao

}