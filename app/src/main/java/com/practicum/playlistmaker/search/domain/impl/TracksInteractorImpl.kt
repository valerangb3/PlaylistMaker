package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.TracksInteractor
import com.practicum.playlistmaker.search.domain.repository.TracksHistoryRepository
import com.practicum.playlistmaker.search.domain.repository.TracksRepository
import java.util.concurrent.Executors

class TracksInteractorImpl(
    private val searchRepository: TracksRepository
): TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            consumer.consume(searchRepository.searchTracks(expression))
        }
    }
}