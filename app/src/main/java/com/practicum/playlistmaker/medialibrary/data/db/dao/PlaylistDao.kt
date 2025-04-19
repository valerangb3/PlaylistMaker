package com.practicum.playlistmaker.medialibrary.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.practicum.playlistmaker.medialibrary.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.medialibrary.data.db.entity.PlaylistTrack
import com.practicum.playlistmaker.medialibrary.data.db.entity.PlaylistTrackRefEntity
import com.practicum.playlistmaker.medialibrary.data.db.entity.TrackEntity

@Dao
interface PlaylistDao {

    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlaylist(playlistEntity: PlaylistEntity): Long

    @Query("SELECT * FROM playlist_table")
    suspend fun getAllPlaylist(): List<PlaylistEntity>

    @Transaction
    @Query("SELECT * FROM playlist_table")
    suspend fun getAllPlayListWithTracks(): List<PlaylistTrack>

    @Transaction
    @Query("SELECT * FROM playlist_table WHERE playlistId = :playlistId")
    suspend fun getPlayListWithTracksById(playlistId: Long): PlaylistTrack

    @Query("SELECT * FROM playlist_table WHERE playlistId = :playlistId")
    suspend fun getPlaylistDetail(playlistId: Long): PlaylistTrack

    @Insert(entity = PlaylistTrackRefEntity::class)
    suspend fun addTrackToPlaylist(row: PlaylistTrackRefEntity)
    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrack(row: TrackEntity): Long
    @Query("SELECT * FROM track_table WHERE trackId = :id")
    suspend fun getTrackById(id: Long): TrackEntity?

    @Delete(entity = PlaylistTrackRefEntity::class)
    suspend fun deleteTrackFromPlaylist(entity: PlaylistTrackRefEntity)
}