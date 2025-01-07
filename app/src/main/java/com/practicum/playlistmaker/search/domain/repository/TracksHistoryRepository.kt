package com.practicum.playlistmaker.search.domain.repository

import com.practicum.playlistmaker.search.domain.models.Track

interface TracksHistoryRepository {
    fun addToHistory(track: Track)
    fun removeTrackHistory()
    fun saveTrackHistory(trackList: MutableList<Track>)
    fun getTrackListHistory(): MutableList<Track>
}