package com.practicum.playlistmaker.medialibrary.data.db.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class PlaylistTrack(
    @Embedded val playlist: PlaylistEntity,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "trackId",
        associateBy = Junction(PlaylistTrackRefEntity::class)
    )
    val trackList: List<TrackEntity>
)
