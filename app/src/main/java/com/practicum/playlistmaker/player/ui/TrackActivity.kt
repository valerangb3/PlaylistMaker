package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityTrackBinding
import com.practicum.playlistmaker.player.domain.models.TrackInfo
import com.practicum.playlistmaker.player.mapper.TrackInfoMapper
import com.practicum.playlistmaker.player.presentation.state.PlayerScreenState
import com.practicum.playlistmaker.player.presentation.viewmodel.PlayerViewModel
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.dpToPx
import com.practicum.playlistmaker.utils.gone
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class TrackActivity : AppCompatActivity() {

    companion object {
        private const val POSTER_RADIUS = 8.0F
    }

    private val args : TrackActivityArgs by navArgs()

    private lateinit var binding: ActivityTrackBinding
    private lateinit var trackItem: Track

    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(trackItem)
    }

    private fun render(isLoading: Boolean, trackInfo: TrackInfo?) {
        binding.progressBar.isVisible = isLoading
        binding.main.isVisible = !isLoading
        trackInfo?.let {
            showContent(it)
        }
    }

    private fun showContent(trackInfo: TrackInfo) {
        binding.timer.text = getString(R.string.timer_value)
        binding.trackName.text = trackInfo.trackName
        binding.artistName.text = trackInfo.artistName
        binding.durationValue.text = trackInfo.trackTime

        if (trackInfo.collectionName?.isNotEmpty() == true) {
            binding.albumValue.text = trackInfo.collectionName
        } else {
            binding.albumName.gone()
            binding.albumValue.gone()
        }

        if (trackInfo.releaseDate?.isNotEmpty() == true) {
            binding.yearValue.text = trackInfo.releaseDate.substringBefore('-')
        } else {
            binding.yearName.gone()
            binding.yearValue.gone()
        }

        if (trackInfo.primaryGenreName?.isNotEmpty() == true) {
            binding.genreValue.text = trackInfo.primaryGenreName
        } else {
            binding.genreName.gone()
            binding.genreValue.gone()
        }

        if (trackInfo.country?.isNotEmpty() == true) {
            binding.countryValue.text = trackInfo.country
        } else {
            binding.countryName.gone()
            binding.countryValue.gone()
        }

        Glide.with(this)
            .load(trackInfo.artworkUrl512)
            .placeholder(R.drawable.big_placeholder)
            .transform(CenterCrop(), RoundedCorners(dpToPx(POSTER_RADIUS, this)))
            .into(binding.poster)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTrackBinding.inflate(layoutInflater)

        setContentView(binding.root)
        trackItem = args.track

        binding.play.setOnClickListener {
            if (trackItem.previewUrl == null) {
                Toast.makeText(this, R.string.play_error, Toast.LENGTH_SHORT).show()
            }
            viewModel.playback()
        }

        binding.buttonBack.setOnClickListener {
            finish()
        }


        binding.toFavourite.setOnClickListener {
            viewModel.addToFavourite(TrackInfoMapper.toTrackInfoMapper(trackItem))
        }

        viewModel.getScreenStateLiveData().observe(this) { screenState ->
            when (screenState) {
                is PlayerScreenState.Loading -> {
                    render(isLoading = true, trackInfo = null)
                }
                is PlayerScreenState.Content -> {
                    render(isLoading = false, trackInfo = screenState.trackInfo)
                }
            }
        }

        viewModel.getPlayStatusLiveData().observe(this) { playStatus ->

            if (!playStatus.isPlaying) {
                binding.play.setImageResource(R.drawable.play)
            } else {
                binding.play.setImageResource(R.drawable.pause)
            }
            binding.timer.text = playStatus.formatProgress()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }
}