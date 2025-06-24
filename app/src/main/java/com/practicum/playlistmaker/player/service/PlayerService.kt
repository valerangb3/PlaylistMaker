package com.practicum.playlistmaker.player.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder

class PlayerService : Service(), IPlayer {

    private var mediaPlayer: MediaPlayer? = null

    private val binder = PlayerServiceBinder()

    private fun releasePlayer() {
        mediaPlayer?.stop()
        mediaPlayer?.setOnPreparedListener(null)
        mediaPlayer?.setOnCompletionListener(null)
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun getPlaterState() {
        TODO("Not yet implemented")
    }

    override fun pausePlayer() {
        mediaPlayer?.pause()
    }

    override fun startPlater() {
        mediaPlayer?.start()
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
    }

    inner class PlayerServiceBinder() : Binder() {
        fun getService(): PlayerService {
            return this@PlayerService
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }
}