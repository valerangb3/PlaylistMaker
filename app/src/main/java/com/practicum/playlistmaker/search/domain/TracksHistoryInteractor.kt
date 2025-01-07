package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.domain.models.Track

interface TracksHistoryInteractor {
    fun saveHistory(trackList: List<Track>)
    fun getHistory(): List<Track>
    fun remove()
    fun addToHistory(track: Track)
}