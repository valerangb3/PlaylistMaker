package com.practicum.playlistmaker.search.domain.repository

import com.practicum.playlistmaker.search.domain.models.Track

interface TracksHistoryRepository {
    suspend fun removeTrackHistory()
    suspend fun getTrackListHistory(): MutableList<Track>
    suspend fun saveTrackHistory(trackList: MutableList<Track>)

    fun addToHistory(track: Track)
}