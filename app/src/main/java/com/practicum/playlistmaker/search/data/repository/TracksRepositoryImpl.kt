package com.practicum.playlistmaker.search.data.repository

import com.practicum.playlistmaker.medialibrary.domain.favourite.FavouriteRepository
import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.dto.TrackDto
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.search.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.search.domain.repository.TracksRepository
import com.practicum.playlistmaker.search.domain.models.Resource
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val favouriteRepository: FavouriteRepository
) : TracksRepository {
    companion object {
        const val HTTP_OK = 200
        const val CLIENT_ERROR = 400
        const val SERVER_ERROR = 500
    }


    private fun convert(trackList: List<TrackDto>): Flow<List<Track>> = flow {
        favouriteRepository.getFavouriteIdList().collect { trackIdList ->
            emit(
                trackList.filter {
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
                        previewUrl = it.previewUrl,

                        inFavourite = trackIdList.contains(it.trackId)
                    )

                }
            )
        }
    }

    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        when (response.resultCode) {
            SERVER_ERROR -> {
                emit(
                    value = Resource.ServerError(
                        status = SERVER_ERROR
                    )
                )
            }

            CLIENT_ERROR -> {
                emit(
                    value = Resource.ClientError(
                        status = CLIENT_ERROR
                    )
                )
            }

            else -> {
                if (response is TracksSearchResponse) {
                    val tracksList = convert(response.results)

                    tracksList.collect { list ->
                        if (list.isEmpty()) {
                            emit(
                                value = Resource.ClientError(
                                    status = CLIENT_ERROR
                                )
                            )
                        } else {
                            emit(
                                value = Resource.Success(
                                    status = HTTP_OK,
                                    data = list
                                )
                            )
                        }
                    }

                } else {
                    emit(
                        value = Resource.ClientError(
                            status = CLIENT_ERROR
                        )
                    )
                }
            }
        }
    }
}