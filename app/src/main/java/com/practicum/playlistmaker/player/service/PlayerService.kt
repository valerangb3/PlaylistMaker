package com.practicum.playlistmaker.player.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.player.domain.models.PlayStatus
import com.practicum.playlistmaker.player.domain.models.TrackInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.ext.android.inject


class PlayerService : Service(), IPlayer {

    private val playerUserCase: PlayerInteractor by inject()

    private val _playStatusStateFlow = MutableStateFlow(PlayStatus(progress = 0L, isPlaying = false))
    private val playStatusStateFlow = _playStatusStateFlow.asStateFlow()


    private companion object {
        const val SERVICE_NOTIFICATION_ID = 7
        const val NOTIFICATION_CHANNEL_ID = "music_service_channel"
        const val NAME_SERVICE = "Player service"
    }

    private val binder = PlayerServiceBinder()

    private var track: TrackInfo? = null

    private fun getCurrentPlayStatus(): PlayStatus = _playStatusStateFlow.value

    private fun createServiceNotification(): Notification {
        var artistName = ""
        track?.artistName?.let {
            artistName = it.ifEmpty {
                this.getString(R.string.empty_artist_name)
            }
        }
        var trackName = ""
        track?.trackName?.let {
            trackName = it.ifEmpty {
                this.getString(R.string.empty_track_name)
            }
        }

        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(this.getString(R.string.app_name))
            .setContentText("$artistName - $trackName")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }

    private fun releasePlayer() {
        playerUserCase.pause()
    }

    private fun initPlayer() {
        if (track?.previewUrl == null) {
            return
        }
        track?.previewUrl?.let {
            playerUserCase.prepareTrack(
                url = it,
                events = object : PlayerInteractor.TrackHandler {
                    override fun onProgress(progress: Long) {
                        _playStatusStateFlow.value = getCurrentPlayStatus().copy(progress = progress)
                    }

                    override fun onComplete() {
                        _playStatusStateFlow.value = getCurrentPlayStatus().copy(progress = 0L, isPlaying = false)
                        hideNotification()
                    }

                    override fun onPause(isPlaying: Boolean) {
                        _playStatusStateFlow.value = getCurrentPlayStatus().copy(isPlaying = isPlaying)
                    }

                    override suspend fun onStart(isPlaying: Boolean) {
                        _playStatusStateFlow.value = getCurrentPlayStatus().copy(
                            isPlaying = true
                        )
                    }

                    override fun onLoad() {
                        _playStatusStateFlow.value = getCurrentPlayStatus().copy(
                            progress = 0L,
                            isPlaying = false,
                            isPrepared = true
                        )
                    }
                }
            )
        }

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NAME_SERVICE,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun getForegroundServiceTypeConstant(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        } else {
            0
        }
    }

    override fun pausePlayer() {
        pause()
    }

    override fun onBind(intent: Intent?): IBinder {
        track = intent?.getSerializableExtra("track") as? TrackInfo
        initPlayer()
        createNotificationChannel()
        //showNotification()

        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        releasePlayer()
        return super.onUnbind(intent)
    }

    override fun getPlayerStatus(): StateFlow<PlayStatus> {
        return playStatusStateFlow
    }

    override suspend fun playback() {
        playerUserCase.playback()
    }

    override fun showNotification() {
        ServiceCompat.startForeground(
            this,
            SERVICE_NOTIFICATION_ID,
            createServiceNotification(),
            getForegroundServiceTypeConstant()
        )
    }

    override fun hideNotification() {
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
    }

    private fun pause() {
        playerUserCase.pause()
    }

    inner class PlayerServiceBinder() : Binder() {
        fun getService(): PlayerService {
            return this@PlayerService
        }
    }
}