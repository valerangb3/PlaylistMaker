package com.practicum.playlistmaker.player.domain.impl

import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.player.domain.repository.PlayerRepository

class PlayerInteractorImpl(
    private val manager: PlayerRepository
) : PlayerInteractor {

    override fun pause() {
        manager.pausePlayer()
    }

    override fun prepareTrack(url: String, events: PlayerInteractor.TrackHandler) {
        manager.prepare(
            url = url,
            event = events
        )
    }

    override fun playback() {
        manager.playbackControl()
    }

    override fun release() {
        manager.release()
    }


}