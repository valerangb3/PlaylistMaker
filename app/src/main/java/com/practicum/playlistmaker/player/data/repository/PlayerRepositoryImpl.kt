package com.practicum.playlistmaker.player.data.repository

import android.media.MediaPlayer
import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.player.domain.repository.PlayerRepository
import kotlinx.coroutines.delay

class PlayerRepositoryImpl(
    private val mediaPlayer: MediaPlayer
) : PlayerRepository {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3

        private const val CHECK_TIME_DELAY = 300L
    }

    private var playerState = STATE_DEFAULT

    private lateinit var eventHandler: PlayerInteractor.TrackHandler

    override fun prepare(url: String, eventHandler: PlayerInteractor.TrackHandler) {
        this.eventHandler = eventHandler
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
            this.eventHandler.onLoad()
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            this.eventHandler.onComplete()
        }
    }

    private suspend fun startPlayer() {
        playerState = STATE_PLAYING
        mediaPlayer.start()
        eventHandler.onStart(isPlaying = mediaPlayer.isPlaying)
        while (mediaPlayer.isPlaying) {
            delay(CHECK_TIME_DELAY)
            eventHandler.onProgress(progress = mediaPlayer.currentPosition.toLong())
        }
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        eventHandler.onPause(isPlaying = mediaPlayer.isPlaying)
    }

    override fun release() {
        mediaPlayer.release()
    }

    override suspend fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }
}