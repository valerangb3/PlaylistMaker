package com.practicum.playlistmaker.medialibrary.data.db.entity

import androidx.room.Entity

@Entity(tableName = "playlist_track_ref", primaryKeys = ["playlistId", "trackId"])
data class PlaylistTrackRefEntity(
    val playlistId: Long,
    val trackId: Long
)
