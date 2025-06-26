package com.practicum.playlistmaker.player.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.player.domain.models.PlayStatus
import com.practicum.playlistmaker.player.presentation.state.PlayerScreenState
import com.practicum.playlistmaker.player.service.state.PlayerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

import java.text.SimpleDateFormat
import java.util.Locale

class PlayerService : Service(), IPlayer {

    private val playerUserCase: PlayerInteractor by inject()

    private val _playStatusStateFlow = MutableStateFlow(PlayStatus(progress = 0L, isPlaying = false))
    val playStatusStateFlow = _playStatusStateFlow.asStateFlow()


    private var playerServiceState: PlayerState = PlayerState.Default()
    private val serviceScope = CoroutineScope(Dispatchers.Default)

    private var timerJob: Job? = null

    private val _playerStateFlow = MutableStateFlow<PlayerState>(PlayerState.Default())
    val playerStateFlow = _playerStateFlow.asStateFlow()

    private companion object {
        const val TAG = "SPRINT25"
        const val SERVICE_NOTIFICATION_ID = 7
        const val NOTIFICATION_CHANNEL_ID = "music_service_channel"
    }

    private var mediaPlayer: MediaPlayer? = null

    private val binder = PlayerServiceBinder()

    private var songUrl = ""

    private fun getCurrentPlayStatus(): PlayStatus = _playStatusStateFlow.value

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer?.currentPosition) ?: "00:00"
    }

    private fun createServiceNotification(): Notification {
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Music foreground service")
            .setContentText("Our service is working right now!")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }

    private fun releasePlayer() {
        playerUserCase.pause()
        /*timerJob?.cancel()
        if (mediaPlayer != null) {
            Log.d(TAG, "releasePlayer")
            mediaPlayer?.stop()
            mediaPlayer?.setOnPreparedListener(null)
            mediaPlayer?.setOnCompletionListener(null)
            mediaPlayer?.release()
            mediaPlayer = null
        }*/
    }

    private fun initPlayer() {
        if (songUrl.isEmpty()) return

        playerUserCase.prepareTrack(
            url = songUrl,
            events = object : PlayerInteractor.TrackHandler {
                override fun onProgress(progress: Long) {
                    _playStatusStateFlow.value = getCurrentPlayStatus().copy(progress = progress)
                }

                override fun onComplete() {
                    timerJob?.cancel()
                    _playStatusStateFlow.value = getCurrentPlayStatus().copy(progress = 0L, isPlaying = false)
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

        if (false) {
            mediaPlayer?.setDataSource(songUrl)
            mediaPlayer?.prepareAsync()
            mediaPlayer?.setOnPreparedListener {
                Log.d(TAG, "Media player prepared")
                playerServiceState = PlayerState.Prepared()
                _playerStateFlow.value = playerServiceState
            }
            mediaPlayer?.setOnCompletionListener {
                Log.d(TAG, "Playback completed")
                playerServiceState = PlayerState.Prepared()
                _playerStateFlow.value = playerServiceState
                timerJob?.cancel()
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Music service",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "Service for playing music"

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

    //override fun getPlayerState(): PlayerState = playerServiceState

    override fun pausePlayer() {
        if (false) {
            mediaPlayer?.pause()
            playerServiceState = PlayerState.Paused(getCurrentPlayerPosition())
            _playerStateFlow.value = playerServiceState
            timerJob?.cancel()
        }
    }

    /*override fun startPlayer() {
        if (false) {
            mediaPlayer?.start()
            playerServiceState = PlayerState.Playing(getCurrentPlayerPosition())
            _playerStateFlow.value = playerServiceState
            startTimer()
            Log.d(TAG, "Мы запустили трек из сервиса")
        }
    }*/

    override fun onBind(intent: Intent?): IBinder {
        songUrl = intent?.getStringExtra("song_url") ?: ""
        initPlayer()
        createNotificationChannel()
        ServiceCompat.startForeground(
            this,
            SERVICE_NOTIFICATION_ID,
            createServiceNotification(),
            getForegroundServiceTypeConstant()
        )

        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        releasePlayer()
        return super.onUnbind(intent)
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
    }

    override suspend fun playback() {
        playerUserCase.playback()
    }

    fun pause() {
        playerUserCase.pause()
        //timerJob?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        //releasePlayer()
    }

    inner class PlayerServiceBinder() : Binder() {
        fun getService(): PlayerService {
            return this@PlayerService
        }
    }
}