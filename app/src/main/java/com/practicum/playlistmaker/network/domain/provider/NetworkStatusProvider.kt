package com.practicum.playlistmaker.network.domain.provider

interface NetworkStatusProvider {
    fun isConnected(): Boolean
}