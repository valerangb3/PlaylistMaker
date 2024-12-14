package com.practicum.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.data.Track
import com.practicum.playlistmaker.utils.dpToPx
import com.practicum.playlistmaker.utils.gone
import java.text.SimpleDateFormat
import java.util.Locale

class TrackActivity : AppCompatActivity() {

    private lateinit var backButton: ImageButton
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var duration: TextView
    private lateinit var durationValue: TextView
    private lateinit var albumName: TextView
    private lateinit var albumNameValue: TextView
    private lateinit var yearName: TextView
    private lateinit var yearValue: TextView
    private lateinit var genreName: TextView
    private lateinit var genreValue: TextView
    private lateinit var countryName: TextView
    private lateinit var countryValue: TextView
    private lateinit var poster: ImageView
    private lateinit var playButton: ImageButton
    private lateinit var myHandler: Handler
    private lateinit var timer: TextView

    private val trackRunnable = Runnable {
        changeTrackViewPosition()
    }
    private var mediaPlayer = MediaPlayer()

    private var playerState = STATE_DEFAULT

    companion object {
        private const val POSTER_RADIUS = 8.0F
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val CHECK_TIME_DELAY = 400L
    }

    private fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url).toString()
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playButton.setImageResource(R.drawable.play)
            playerState = STATE_PREPARED
            myHandler.removeCallbacks(trackRunnable)
            timer.text = getFormatPosition(0L)
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setImageResource(R.drawable.pause)
        playerState = STATE_PLAYING
        myHandler.postDelayed(trackRunnable, CHECK_TIME_DELAY)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton.setImageResource(R.drawable.play)
        playerState = STATE_PAUSED
        myHandler.removeCallbacks(trackRunnable)
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun changeTrackViewPosition() {
        timer.text = getFormatPosition(mediaPlayer.currentPosition.toLong())
        myHandler.postDelayed(trackRunnable, CHECK_TIME_DELAY)
    }

    private fun getFormatPosition(time: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track)

        myHandler = Handler(Looper.getMainLooper())
        val trackItem = intent.getSerializableExtra(Track::class.simpleName) as Track

        backButton = findViewById(R.id.buttonBack)
        trackName = findViewById(R.id.trackName)
        artistName = findViewById(R.id.artistName)
        duration = findViewById(R.id.durationName)
        durationValue = findViewById(R.id.durationValue)
        albumName = findViewById(R.id.albumName)
        albumNameValue = findViewById(R.id.albumValue)
        yearName = findViewById(R.id.yearName)
        yearValue = findViewById(R.id.yearValue)
        genreName = findViewById(R.id.genreName)
        genreValue = findViewById(R.id.genreValue)
        countryName = findViewById(R.id.countryName)
        countryValue = findViewById(R.id.countryValue)
        poster = findViewById(R.id.poster)
        playButton = findViewById(R.id.play)
        timer = findViewById(R.id.timer)

        playButton.setOnClickListener {
            if (trackItem.previewUrl == null) {
                Toast.makeText(this, R.string.play_error, Toast.LENGTH_SHORT).show()
            } else {
                playbackControl()
            }
        }

        Glide.with(this)
            .load(trackItem.getCoverArtwork())
            .placeholder(R.drawable.big_placeholder)
            .transform(CenterCrop(), RoundedCorners(dpToPx(POSTER_RADIUS, this)))
            .into(poster)

        backButton.setOnClickListener {
            finish()
        }
        getFormatPosition(trackItem.trackTimeMillis)
        timer.text = getString(R.string.timer_value)
        trackName.text = trackItem.trackName
        artistName.text = trackItem.artistName
        durationValue.text = getFormatPosition(trackItem.trackTimeMillis)

        if (trackItem.collectionName?.isNotEmpty() == true) {
            albumNameValue.text = trackItem.collectionName
        } else {
            albumName.gone()
            albumNameValue.gone()
        }

        if (trackItem.releaseDate?.isNotEmpty() == true) {
            yearValue.text = trackItem.releaseDate.substringBefore('-')
        } else {
            yearName.gone()
            yearValue.gone()
        }

        if (trackItem.primaryGenreName?.isNotEmpty() == true) {
            genreValue.text = trackItem.primaryGenreName
        } else {
            genreName.gone()
            genreValue.gone()
        }

        if (trackItem.country?.isNotEmpty() == true) {
            countryValue.text = trackItem.country
        } else {
            countryName.gone()
            countryValue.gone()
        }
        trackItem.previewUrl?.let {
            preparePlayer(trackItem.previewUrl)
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        myHandler.removeCallbacks(trackRunnable)
        mediaPlayer.release()
    }
}