package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.TracksHistoryInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.repository.TracksHistoryRepository

class TracksHistoryInteractorImpl(
    private val tracksHistoryRepository: TracksHistoryRepository
) : TracksHistoryInteractor {

    override fun getHistory(): List<Track> = tracksHistoryRepository.getTrackListHistory()

    override fun saveHistory(trackList: List<Track>) {
        tracksHistoryRepository.saveTrackHistory(trackList.toMutableList())
    }

    override fun addToHistory(track: Track) {
        tracksHistoryRepository.addToHistory(track)
    }

    override fun remove() {
        tracksHistoryRepository.removeTrackHistory()
    }
}