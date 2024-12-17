package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.data.PlayerManager
import com.practicum.playlistmaker.domain.api.PlayerInteractor

class PlayerInteractorImpl(
    private val manager: PlayerManager
): PlayerInteractor {

    private lateinit var startHandler: () -> Unit
    private lateinit var pauseHandler: () -> Unit
    private lateinit var completeHandler: () -> Unit

    private var playerState = STATE_DEFAULT

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

    override fun pauseTrack() {
        manager.pausePlayer()
        playerState = STATE_PAUSED
        pauseHandler()
    }

    override fun getCurrentPosition(): Long = manager.getCurrentPosition()

    override fun startTrack() {
        manager.startPlayer()
        playerState = STATE_PLAYING
        startHandler()
    }

    override fun prepare(url: String) {
        if (playerState == STATE_DEFAULT) {
            manager.prepare(url) {
                playerState = STATE_PREPARED
            }

            manager.setCompleteHandler {
                playerState = STATE_PREPARED
                completeHandler()
            }
        }
    }

    override fun playbackControl(start: () -> Unit, pause: () -> Unit, complete: () -> Unit) {
        startHandler = start
        pauseHandler = pause
        completeHandler = complete
        when (playerState) {
            STATE_PLAYING -> {
                pauseTrack()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startTrack()
            }
        }
    }

    override fun release() {
        manager.release()
    }


}