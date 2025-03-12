package com.practicum.playlistmaker.player.domain.repository

import com.practicum.playlistmaker.player.domain.PlayerInteractor

interface PlayerRepository {
    fun prepare(url: String, eventHandler: PlayerInteractor.TrackHandler)
    fun getCurrentPosition(): Long
    //fun startPlayer()
    fun pausePlayer()
    fun release()
    suspend fun playbackControl()
}