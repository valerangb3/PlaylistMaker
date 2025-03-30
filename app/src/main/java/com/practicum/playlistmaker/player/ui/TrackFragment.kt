package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentTrackBinding
import com.practicum.playlistmaker.player.domain.models.TrackInfo
import com.practicum.playlistmaker.player.presentation.state.PlayerScreenState
import com.practicum.playlistmaker.player.presentation.viewmodel.PlayerViewModel
import com.practicum.playlistmaker.utils.dpToPx
import com.practicum.playlistmaker.utils.gone
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class TrackFragment : Fragment() {
    companion object {
        private const val POSTER_RADIUS = 8.0F
    }

    private var _binding : FragmentTrackBinding? = null
    private val binding get() = _binding!!

    private val args : TrackFragmentArgs by navArgs()

    private lateinit var trackItem: TrackInfo

    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(trackItem)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrackBinding.inflate(inflater, container, false)
        return binding.root
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
            .transform(CenterCrop(), RoundedCorners(dpToPx(POSTER_RADIUS, requireContext())))
            .into(binding.poster)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trackItem = args.track

        binding.play.setOnClickListener {
            if (trackItem.previewUrl == null) {
                Toast.makeText(requireContext(), R.string.play_error, Toast.LENGTH_SHORT).show()
            }
            viewModel.playback()
        }

        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }


        binding.toFavourite.setOnClickListener {
            viewModel.favouriteHandler(trackItem/*TrackInfoMapper.toTrackInfoMapper(trackItem)*/)
        }

        viewModel.inFavouriteLiveData().observe(viewLifecycleOwner) { inFavourite ->
            if (inFavourite) {
                binding.toFavourite.setImageResource(R.drawable.in_favourite)
                trackItem.inFavourite = inFavourite
            } else {
                binding.toFavourite.setImageResource(R.drawable.favourite)
                trackItem.inFavourite = inFavourite
            }
        }

        viewModel.getScreenStateLiveData().observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is PlayerScreenState.Loading -> {
                    render(isLoading = true, trackInfo = null)
                }
                is PlayerScreenState.Content -> {
                    render(isLoading = false, trackInfo = screenState.trackInfo)
                }
            }
        }

        viewModel.getPlayStatusLiveData().observe(viewLifecycleOwner) { playStatus ->

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}