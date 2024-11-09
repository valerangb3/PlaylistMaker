package com.practicum.playlistmaker

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.data.Track
import com.practicum.playlistmaker.track.adapter.TrackListAdapter.TrackViewHolder.Companion.IMG_RADIUS
import com.practicum.playlistmaker.utils.dpToPx
import com.practicum.playlistmaker.utils.gone
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.time.Duration

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track)

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

        val trackItem = intent.getSerializableExtra(Track::class.simpleName) as Track

        Glide.with(this)
            .load(trackItem.getCoverArtwork())
            .placeholder(R.drawable.big_placeholder)
            .centerInside()
            .transform(RoundedCorners(dpToPx(POSTER_RADIUS, this)))
            .into(poster)

        backButton.setOnClickListener {
            finish()
        }

        trackName.text = trackItem.trackName
        artistName.text = trackItem.artistName
        durationValue.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackItem.trackTimeMillis)

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
    }

    companion object {
        const val POSTER_RADIUS = 8.0F
    }
}