package com.practicum.playlistmaker.medialibrary.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.practicum.playlistmaker.medialibrary.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.medialibrary.data.db.entity.PlaylistTrack
import com.practicum.playlistmaker.medialibrary.data.db.entity.PlaylistTrackRefEntity
import com.practicum.playlistmaker.medialibrary.data.db.entity.TrackEntity

@Dao
interface PlaylistDao {

    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlaylist(playlistEntity: PlaylistEntity): Long

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

    @Query("DELETE FROM track_table WHERE trackId = :trackId")
    suspend fun deleteTrackItemById(trackId: Long)

    @Delete(entity = PlaylistTrackRefEntity::class)
    suspend fun deleteTrackFromPlaylist(entity: PlaylistTrackRefEntity)

    @Query("SELECT playlistId, trackId FROM playlist_track_ref WHERE playlistId = :playlistId")
    suspend fun getPlaylistTrackRef(playlistId: Long): List<PlaylistTrackRefEntity>

    @Query("SELECT trackId FROM playlist_track_ref WHERE trackId = :trackId")
    suspend fun getTrackListRef(trackId: Long): List<Long>

    @Query("SELECT trackId FROM playlist_track_ref WHERE trackId IN (:tracksId)")
    suspend fun getTrackListRefByListId(tracksId: List<Long>): List<Long>

    @Delete(entity = PlaylistTrackRefEntity::class)
    suspend fun deletePlaylistRefs(items: List<PlaylistTrackRefEntity>)

    @Delete(entity = PlaylistEntity::class)
    suspend fun deletePlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM playlist_table WHERE playlistId = :playlistId")
    suspend fun getPlaylist(playlistId: Long): PlaylistEntity

    @Update(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlaylist(playlistEntity: PlaylistEntity)
}