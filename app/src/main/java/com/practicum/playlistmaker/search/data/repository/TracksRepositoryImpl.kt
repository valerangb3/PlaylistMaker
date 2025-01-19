package com.practicum.playlistmaker.search.data.repository

import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.search.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.search.domain.repository.TracksRepository
import com.practicum.playlistmaker.search.domain.models.Resource
import com.practicum.playlistmaker.search.domain.models.Track

class TracksRepositoryImpl(
    private val networkClient: NetworkClient
) : TracksRepository {
    companion object {
        const val CLIENT_ERROR = 404
        const val SERVER_ERROR = 500
    }

    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        return when (response.resultCode) {
            SERVER_ERROR -> {
                Resource.ServerError(
                    status = SERVER_ERROR
                )
            }
            CLIENT_ERROR -> {
                Resource.ClientError(
                    status = CLIENT_ERROR
                )
            }
            else -> {
                if (response is TracksSearchResponse) {
                    val tracksList = response.results.filter {
                        it.trackName.isNotEmpty() &&
                                it.artistName.isNotEmpty() &&
                                it.trackTimeMillis > 0
                    }.map {
                        Track(
                            trackId = it.trackId,
                            trackName = it.trackName,
                            artistName = it.artistName,
                            trackTime = it.getFormatTime(),
                            artworkUrl100 = it.artworkUrl100,
                            collectionName = it.collectionName,
                            releaseDate = it.releaseDate,
                            primaryGenreName = it.primaryGenreName,
                            country = it.country,
                            previewUrl = it.previewUrl
                        )

                    }
                    if (tracksList.isEmpty()) {
                        Resource.ClientError(
                            status = 404
                        )
                    } else {
                        Resource.Success(
                            data = tracksList
                        )
                    }
                } else {
                    Resource.ClientError(
                        status = CLIENT_ERROR
                    )
                }
            }
        }
    }
}