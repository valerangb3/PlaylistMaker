package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentTrackBinding
import com.practicum.playlistmaker.player.domain.models.TrackInfo
import com.practicum.playlistmaker.player.presentation.state.PlayerScreenState
import com.practicum.playlistmaker.player.presentation.state.PlaylistScreenState
import com.practicum.playlistmaker.player.presentation.state.ToastState
import com.practicum.playlistmaker.player.presentation.viewmodel.PlayerViewModel
import com.practicum.playlistmaker.player.ui.models.PlaylistTrack
import com.practicum.playlistmaker.player.ui.models.Track
import com.practicum.playlistmaker.utils.dpToPx
import com.practicum.playlistmaker.utils.gone
import com.practicum.playlistmaker.utils.show
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class TrackFragment : Fragment() {
    companion object {
        private const val POSTER_RADIUS = 8.0F
    }

    private var _binding: FragmentTrackBinding? = null
    private val binding get() = _binding!!

    private val args: TrackFragmentArgs by navArgs()

    private lateinit var trackItem: TrackInfo

    private var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>? = null

    private lateinit var playlistAdapter: PlaylistAdapter

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

    private fun trackInfoToTrack(track: TrackInfo): Track {
        return Track(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTime = track.trackTime,
            artworkUrl512 = track.artworkUrl512,
            collectionName = track.collectionName ?: "",
            releaseDate = track.releaseDate ?: "",
            primaryGenreName = track.primaryGenreName ?: "",
            country = track.country ?: "",
            previewUrl = track.previewUrl ?: ""
        )
    }

    private fun render(isLoading: Boolean, trackInfo: TrackInfo?) {
        binding.progressBar.isVisible = isLoading
        binding.main.isVisible = !isLoading
        trackInfo?.let {
            showContent(it)
        }
    }

    private fun initRecyclerView() {
        playlistAdapter = PlaylistAdapter { playlistTrack ->
            viewModel.addToPlayList(playlistTrack, trackInfoToTrack(trackItem))
        }
        binding.playlist.adapter = playlistAdapter
        binding.playlist.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
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

    private fun handleBottomSheet(bottomSheetBehavior: BottomSheetBehavior<LinearLayout>?) {
        binding.toPlaylist.setOnClickListener {
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        bottomSheetBehavior?.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        viewModel.getPlaylistItems()
                        binding.overlay.show()
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.gone()
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                val normalizedAlpha = ((slideOffset + 1f) / 2f).coerceIn(0f, 1f)
                binding.overlay.alpha = normalizedAlpha

                if (normalizedAlpha > 0f) {
                    binding.overlay.show()
                } else {
                    binding.overlay.gone()
                }
            }

        })
    }

    private fun initBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        handleBottomSheet(bottomSheetBehavior)
    }

    private fun handleListAdapter(playlistTrack: PlaylistTrack) {
        playlistAdapter.playlistItems.forEachIndexed { index, playlistTrackItem ->
            if (playlistTrackItem.playlistId == playlistTrack.playlistId) {
                playlistTrackItem.trackCount = playlistTrack.trackCount
                playlistAdapter.notifyItemChanged(index)
            }
        }
    }

    private fun addToAdapter(items: List<PlaylistTrack>) {
        playlistAdapter.playlistItems.clear()
        playlistAdapter.playlistItems.addAll(items)
        playlistAdapter.notifyDataSetChanged()

    }

    private fun showPlaylistContent(playlist: List<PlaylistTrack>) {
        hideLoading()
        showContentView()
        binding.playlist.show()
        addToAdapter(playlist)
    }

    private fun hideContentView() {
        binding.roundedRectangle.gone()
        binding.playlistTitle.gone()
        binding.create.gone()
    }

    private fun showContentView() {
        binding.roundedRectangle.show()
        binding.playlistTitle.show()
        binding.create.show()
    }

    private fun showLoading() {
        hideContentView()
        binding.playlist.gone()
        hideEmptyContent()
        binding.playlistProgressBar.show()
    }

    private fun hideLoading() {
        binding.playlistProgressBar.gone()
        binding.playlist.show()
    }

    private fun hideEmptyContent() {
        hideLoading()
        binding.playlist.show()
        binding.playlistErrorContainer.gone()
    }

    private fun showEmptyContent() {
        showContentView()
        hideLoading()
        binding.playlist.gone()
        binding.playlistErrorContainer.show()
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
            viewModel.favouriteHandler(trackItem)
        }
        binding.create.setOnClickListener {
            findNavController().navigate(R.id.action_trackFragment_to_playlistMakerFragment)
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
        viewModel.getScreenPlaylistStateLiveData().observe(viewLifecycleOwner) { playlistState ->
            when (playlistState) {
                is PlaylistScreenState.Loading -> showLoading()
                is PlaylistScreenState.PlaylistScreenEmptyContentState -> showEmptyContent()
                is PlaylistScreenState.PlaylistScreenContentState -> {
                    showPlaylistContent(
                        playlistState.data
                    )
                }
                is PlaylistScreenState.PlaylistContentState -> handleListAdapter(playlistState.data)
            }
        }
        viewModel.observeToastState().observe(viewLifecycleOwner) { toastState ->
            if (toastState is ToastState.Show) {
                //Если трек добавлен в плейлист
                if (toastState.inPlaylist) {
                    Toast.makeText(
                        requireContext(),
                        requireContext().getString(R.string.playlist_add_track, toastState.additionalMessage),
                        Toast.LENGTH_SHORT
                    ).show()
                    bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
                } else {
                    Toast.makeText(
                        requireContext(),
                        requireContext().getString(R.string.playlist_track_already_add, toastState.additionalMessage),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                viewModel.toastWasShown()
            }
        }

        initBottomSheet()
        initRecyclerView()
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bottomSheetBehavior = null
        _binding = null
    }
}