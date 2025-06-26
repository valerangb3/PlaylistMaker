package com.practicum.playlistmaker.player.service

import com.practicum.playlistmaker.player.service.state.PlayerState

interface IPlayer {
    suspend fun playback()
    fun pausePlayer()
   /* fun startPlayer()

    fun pausePlayer()

    fun getPlayerState(): PlayerState*/
}