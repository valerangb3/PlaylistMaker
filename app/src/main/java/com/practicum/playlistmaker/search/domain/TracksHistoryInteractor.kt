package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksHistoryInteractor {
    suspend fun remove()
    suspend fun saveHistory(trackList: List<Track>)

    fun getHistory(): Flow<List<Track>>
    fun addToHistory(track: Track)
}