package com.practicum.playlistmaker.search.domain.models

sealed interface Resource<T> {
    data class Success<T>(val data: T) : Resource<T>
    data class ClientError<T>(val status: Int) : Resource<T>
    data class ServerError<T>(val status: Int) : Resource<T>
}