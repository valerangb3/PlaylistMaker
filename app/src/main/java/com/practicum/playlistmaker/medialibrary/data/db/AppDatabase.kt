package com.practicum.playlistmaker.medialibrary.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.medialibrary.data.db.dao.FavouriteTrackDao
import com.practicum.playlistmaker.medialibrary.data.db.dao.PlaylistDao
import com.practicum.playlistmaker.medialibrary.data.db.entity.FavouriteTrackEntity
import com.practicum.playlistmaker.medialibrary.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.medialibrary.data.db.entity.PlaylistTrackRefEntity
import com.practicum.playlistmaker.medialibrary.data.db.entity.TrackEntity

@Database(
    version = 7, entities = [
        FavouriteTrackEntity::class,
        PlaylistEntity::class,
        TrackEntity::class,
        PlaylistTrackRefEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favouriteTrackDao(): FavouriteTrackDao

    abstract fun playlistDao(): PlaylistDao

}