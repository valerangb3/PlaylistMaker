package com.practicum.playlistmaker.player.service

import com.practicum.playlistmaker.player.domain.models.PlayStatus
import kotlinx.coroutines.flow.StateFlow


interface IPlayer {
    suspend fun playback()
    fun pausePlayer()
    fun getPlayerStatus(): StateFlow<PlayStatus>
    fun showNotification()
    fun hideNotification()
}