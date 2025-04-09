package com.practicum.playlistmaker.medialibrary.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String = "",
    @ColumnInfo("img_path")
    val pathSrc: String = "",
    val tracks: String = "",
    val trackCount: Int = 0
)