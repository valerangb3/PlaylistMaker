package com.practicum.playlistmaker.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class PlayerService : Service() {

    private val binder = PlayerServiceBinder()

    override fun onBind(intent: Intent?): IBinder = binder

    inner class PlayerServiceBinder() : Binder() {
        fun getService(): PlayerService {
            return this@PlayerService
        }
    }
}