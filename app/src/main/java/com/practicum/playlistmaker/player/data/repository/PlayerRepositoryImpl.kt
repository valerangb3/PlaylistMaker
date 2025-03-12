package com.practicum.playlistmaker.player.data.repository

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.player.domain.repository.PlayerRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

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

    private val myHandler = Handler(Looper.getMainLooper())
    private val trackRunnable = Runnable {
        handleRunnable()
    }

    private fun handleRunnable() {
        if (playerState == STATE_PLAYING) {
            val position = mediaPlayer.currentPosition.toLong()
            //Log.d("SPRINT20", "{position = $position}")
            eventHandler.onProgress(progress = position)
            myHandler.postDelayed(trackRunnable, CHECK_TIME_DELAY)
        }
    }

    override fun getCurrentPosition() : Long {
        var curPosition = 0L
        if (mediaPlayer.isPlaying) {
            curPosition = mediaPlayer.currentPosition.toLong()
        }
        return curPosition
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
            //timerJob?.cancel()
            //myHandler.removeCallbacks(trackRunnable)
            this.eventHandler.onComplete()
        }
    }

    private suspend fun startPlayer() {
        playerState = STATE_PLAYING
        mediaPlayer.start()

        while (mediaPlayer.isPlaying) {
            delay(CHECK_TIME_DELAY)
            eventHandler.onStart(
                isPlaying = mediaPlayer.isPlaying,
                progress = mediaPlayer.currentPosition.toLong()
            )
        }


        /*while (mediaPlayer.isPlaying) {
            Log.d("SPRINT20xxx", "p=${mediaPlayer.currentPosition.toLong()}")
            eventHandler.onProgress(progress = mediaPlayer.currentPosition.toLong())
            delay(300L)
        }*/
        //myHandler.postDelayed(trackRunnable, CHECK_TIME_DELAY)
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        //timerJob?.cancel()
        eventHandler.onPause()
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