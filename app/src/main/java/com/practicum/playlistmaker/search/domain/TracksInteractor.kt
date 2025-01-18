package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.domain.models.Resource
import com.practicum.playlistmaker.search.domain.models.Track

interface TracksInteractor {

    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundTracks: Resource<List<Track>>)
    }
}