package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.TracksHistoryInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.repository.TracksHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksHistoryInteractorImpl(
    private val tracksHistoryRepository: TracksHistoryRepository
) : TracksHistoryInteractor {

    override fun getHistory(): Flow<List<Track>> = flow {
        emit(tracksHistoryRepository.getTrackListHistory())
    }

    override suspend fun saveHistory(trackList: List<Track>) {
        tracksHistoryRepository.saveTrackHistory(trackList.toMutableList())
    }

    override fun addToHistory(track: Track) {
        tracksHistoryRepository.addToHistory(track)
    }

    override suspend fun remove() {
        tracksHistoryRepository.removeTrackHistory()
    }
}