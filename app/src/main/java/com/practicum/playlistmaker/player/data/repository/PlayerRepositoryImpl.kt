package com.practicum.playlistmaker.player.data.repository

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.player.domain.repository.PlayerRepository

class PlayerRepositoryImpl(
    private val mediaPlayer: MediaPlayer
) : PlayerRepository {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3

        private const val CHECK_TIME_DELAY = 500L
    }

    private var playerState = STATE_DEFAULT

    private lateinit var eventHandler: PlayerInteractor.TrackHandler

    private val myHandler = Handler(Looper.getMainLooper())
    private val trackRunnable = Runnable {
        handleRunnable()
    }

    private fun handleRunnable() {
        if (playerState == STATE_PLAYING) {
            val position = mediaPlayer.currentPosition.toLong()
            eventHandler.onProgress(progress = position)
            myHandler.postDelayed(trackRunnable, CHECK_TIME_DELAY)
        }
    }

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
            myHandler.removeCallbacks(trackRunnable)
            this.eventHandler.onComplete()
        }
    }

    override fun startPlayer() {
        playerState = STATE_PLAYING
        mediaPlayer.start()
        eventHandler.onStart()

        myHandler.postDelayed(trackRunnable, CHECK_TIME_DELAY)
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        myHandler.removeCallbacks(trackRunnable)
        eventHandler.onPause()
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun playbackControl() {
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