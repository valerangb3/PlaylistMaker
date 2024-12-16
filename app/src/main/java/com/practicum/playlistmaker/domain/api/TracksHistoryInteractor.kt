package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface TracksHistoryInteractor {
    fun saveHistory(trackList: List<Track>)
    fun getHistory(): List<Track>
    fun remove()
    fun addToHistory(track: Track)
}