package com.practicum.playlistmaker.network.domain

import com.practicum.playlistmaker.network.domain.provider.NetworkStatusProvider

class CheckNetworkUseCase(
    private val networkStatusProvider: NetworkStatusProvider
) {
    fun execute(): Boolean {
        return networkStatusProvider.isConnected()
    }
}