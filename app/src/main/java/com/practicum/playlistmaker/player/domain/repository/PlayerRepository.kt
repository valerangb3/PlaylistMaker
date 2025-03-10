package com.practicum.playlistmaker.player.domain.repository

import com.practicum.playlistmaker.player.domain.PlayerInteractor

interface PlayerRepository {
    fun prepare(url: String, eventHandler: PlayerInteractor.TrackHandler)
    fun startPlayer()
    fun pausePlayer()
    fun release()
    fun playbackControl()
}