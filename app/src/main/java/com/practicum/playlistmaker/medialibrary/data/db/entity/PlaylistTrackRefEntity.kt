package com.practicum.playlistmaker.medialibrary.data.db.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(tableName = "playlist_track_ref", primaryKeys = ["playlistId", "trackId"],  indices = [Index(
    value = ["trackId"]
)])
data class PlaylistTrackRefEntity(
    val playlistId: Long,
    val trackId: Long
)
