package com.practicum.playlistmaker.medialibrary.data.db.repository.playlist

import android.content.Context
import android.os.Environment
import com.practicum.playlistmaker.medialibrary.data.db.AppDatabase
import com.practicum.playlistmaker.medialibrary.data.db.mappers.PlaylistMapper
import com.practicum.playlistmaker.medialibrary.domain.playlist.PlaylistRepository
import com.practicum.playlistmaker.medialibrary.domain.playlist.models.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val context: Context,
    private val mapper: PlaylistMapper
) : PlaylistRepository {


    private fun genNewFileName(pathSrc: String) = md5Hash(File(pathSrc).name)

    private fun md5Hash(salt: String): String {
        val md = MessageDigest.getInstance("MD5")
        val bigInt = BigInteger(1, md.digest(salt.toByteArray(Charsets.UTF_8)))
        return String.format("%032x", bigInt)
    }

    private suspend fun saveImage(fileName: String) {
        withContext(Dispatchers.IO) {
            val filePath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        }
    }

    override suspend fun addPlaylist(item: Playlist) {
        //TODO нужно сгенерировать полный путь к файлу,
        // сохранить его во внешней дериктории и получить путь по котором сохранили
        if (item.pathSrc.isEmpty()) {
            saveImage(genNewFileName(item.pathSrc))
        }
        appDatabase.playlistDao().addPlaylist(mapper.map(item))
    }
}