package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.data.TracksHistoryManager
import com.practicum.playlistmaker.domain.api.TracksHistoryInteractor
import com.practicum.playlistmaker.domain.models.Track

class TracksHistoryInteractorImpl(
    private val tracksHistoryManager: TracksHistoryManager
) : TracksHistoryInteractor {

    override fun getHistory(): List<Track> = tracksHistoryManager.getTrackListHistory()

    override fun saveHistory(trackList: List<Track>) {
        tracksHistoryManager.saveTrackHistory(trackList.toMutableList())
    }

    override fun addToHistory(track: Track) {
        tracksHistoryManager.addToHistory(track)
    }

    override fun remove() {
        tracksHistoryManager.removeTrackHistory()
    }
}