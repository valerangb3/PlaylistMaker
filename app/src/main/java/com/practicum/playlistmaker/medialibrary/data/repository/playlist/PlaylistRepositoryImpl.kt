package com.practicum.playlistmaker.medialibrary.data.repository.playlist

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import androidx.core.net.toUri
import com.practicum.playlistmaker.medialibrary.data.db.AppDatabase
import com.practicum.playlistmaker.medialibrary.data.db.converter.PlaylistConverter
import com.practicum.playlistmaker.medialibrary.data.db.entity.PlaylistTrackRefEntity
import com.practicum.playlistmaker.medialibrary.data.db.mappers.PlaylistMapper
import com.practicum.playlistmaker.medialibrary.domain.playlist.PlaylistRepository
import com.practicum.playlistmaker.medialibrary.domain.playlist.models.Playlist
import com.practicum.playlistmaker.medialibrary.domain.playlist.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.math.BigInteger
import java.security.MessageDigest

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val context: Context,
    private val mapper: PlaylistMapper,
    private val converter: PlaylistConverter
) : PlaylistRepository {


    companion object {
        private const val SUB_FOLDER = "posters"
    }

    private fun genNewFileName(pathSrc: String) = md5Hash(File(pathSrc).name)

    private fun md5Hash(salt: String): String {
        val md = MessageDigest.getInstance("MD5")
        val bigInt = BigInteger(1, md.digest(salt.toByteArray(Charsets.UTF_8)))
        return String.format("%032x", bigInt)
    }

    private fun saveImage(item: Playlist): String {
        val fileName = genNewFileName(item.pathSrc)
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), SUB_FOLDER)
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, fileName)
        val inputStream = context.contentResolver.openInputStream(item.pathSrc.toUri())
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        inputStream?.close()
        return file.toUri().toString()
    }

    override suspend fun addPlaylist(item: Playlist): Long {
        val playlistId = withContext(Dispatchers.IO) {
            if (item.pathSrc.isNotEmpty()) {
                val path = saveImage(item = item)
                item.pathSrc = path
            }
            appDatabase.playlistDao().addPlaylist(mapper.map(item))
        }
        return playlistId
    }

    override suspend fun getPlaylistItems(): Flow<List<Playlist>> = flow {
        val result = withContext(Dispatchers.IO) {
            appDatabase.playlistDao().getAllPlayListWithTracks()
        }
        emit(converter.convert(result))
    }

    override suspend fun getPlaylistItem(playlistId: Long): Flow<Playlist> = flow {
        val result = withContext(Dispatchers.IO) {
            appDatabase.playlistDao().getPlaylistDetail(playlistId)
        }
        val sortedTracks = result.trackList.sortedWith(compareByDescending {
            it.timestamp
        })
        result.trackList = sortedTracks
        emit(converter.convert(result))
    }

    override suspend fun updatePlaylistItem(playlistId: Long, newData: Playlist) {
        withContext(Dispatchers.IO) {
            val oldPlaylist = appDatabase.playlistDao().getPlaylist(playlistId = playlistId)
            val title = newData.title
            val description = newData.description
            var poster = oldPlaylist.pathSrc
            if (newData.pathSrc.isNotEmpty()) {
                if (oldPlaylist.pathSrc.isNotEmpty()) {
                    removePoster(oldPlaylist.pathSrc)
                }
                poster = saveImage(newData)
            }
            appDatabase.playlistDao().updatePlaylist(
                playlistEntity = mapper.map(
                    item = Playlist(
                        id = oldPlaylist.playlistId,
                        title = title,
                        description = description,
                        pathSrc = poster
                    )
                )
            )
        }
    }

    override suspend fun addTrackToPlaylist(playlistId: Long, track: Track): Long {
        withContext(Dispatchers.IO) {
            val rowTrack = appDatabase.playlistDao().getTrackById(track.trackId)
            val trackId = rowTrack?.trackId ?: appDatabase.playlistDao().addTrack(mapper.map(track))
            val playlist = appDatabase.playlistDao().getPlayListWithTracksById(playlistId)
            if (playlist.trackList.none { it.trackId == trackId }) {
                appDatabase.playlistDao().addTrackToPlaylist(
                    PlaylistTrackRefEntity(
                        playlistId = playlistId,
                        trackId = trackId
                    )
                )
            }
        }
        return playlistId
    }

    private fun removePoster(path: String) {
        val file = File(path)
        if (file.exists()) {
            file.delete()
        }
    }

    override suspend fun deletePlaylist(playlistId: Long) {
        withContext(Dispatchers.IO) {
            val itemsRef = appDatabase.playlistDao().getPlaylistTrackRef(playlistId = playlistId)
            val item = appDatabase.playlistDao().getPlaylist(playlistId = playlistId)
            val tracksId = mutableListOf<Long>()

            getPlaylistItem(playlistId = playlistId).collect { playlist ->
                playlist.tracksId.forEach { trackId ->
                    tracksId.add(trackId)
                }
            }

            if (item.pathSrc.isNotEmpty()) {
                removePoster(path = item.pathSrc)
            }
            appDatabase.playlistDao().deletePlaylistRefs(items = itemsRef)
            appDatabase.playlistDao().deletePlaylist(playlistEntity = item)

            if (tracksId.isNotEmpty()) {
                val tracksIdEntity = appDatabase.playlistDao().getTrackListRefByListId(tracksId = tracksId)
                tracksId.forEach { trackId ->
                    deleteTrackItem(trackId = trackId, tracks = tracksIdEntity)
                }
            }
        }
    }

    private suspend fun deleteTrackItem(trackId: Long, tracks: List<Long>) {
        if (!tracks.contains(trackId)) {
            appDatabase.playlistDao().deleteTrackItemById(trackId = trackId)
        }
    }

    override suspend fun deleteTrack(playlistId: Long, trackId: Long) {
        withContext(Dispatchers.IO) {
            appDatabase.playlistDao().deleteTrackFromPlaylist(
                entity = PlaylistTrackRefEntity(
                    playlistId = playlistId,
                    trackId = trackId
                )
            )
            val tracks = appDatabase.playlistDao().getTrackListRef(trackId = trackId)
            deleteTrackItem(trackId = trackId, tracks = tracks)
        }
    }

}