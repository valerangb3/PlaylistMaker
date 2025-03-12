package com.practicum.playlistmaker.player.domain.impl

import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.player.domain.repository.PlayerRepository

class PlayerInteractorImpl(
    private val playerRepository: PlayerRepository
) : PlayerInteractor {

    override fun pause() {
        playerRepository.pausePlayer()
    }

    override fun prepareTrack(url: String, events: PlayerInteractor.TrackHandler) {
        playerRepository.prepare(
            url = url,
            eventHandler = events
        )
    }

    override suspend fun playback() {
        playerRepository.playbackControl()
    }

    override fun release() {
        playerRepository.release()
    }


}