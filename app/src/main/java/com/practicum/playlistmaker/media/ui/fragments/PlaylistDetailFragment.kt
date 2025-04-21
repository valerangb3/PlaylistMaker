package com.practicum.playlistmaker.media.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistDetailBinding
import com.practicum.playlistmaker.media.presentation.state.PlaylistDetailState
import com.practicum.playlistmaker.media.presentation.viewmodel.PlaylistDetailViewModel
import com.practicum.playlistmaker.media.ui.adapter.PlaylistDetailTracksAdapter
import com.practicum.playlistmaker.media.ui.adapter.common.OnPlaylistTrackListeners
import com.practicum.playlistmaker.media.ui.models.PlaylistDetail
import com.practicum.playlistmaker.utils.getTimeWordForm
import com.practicum.playlistmaker.utils.getWordForm
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.practicum.playlistmaker.utils.gone
import com.practicum.playlistmaker.utils.show
import com.practicum.playlistmaker.media.ui.models.Track
import com.practicum.playlistmaker.player.domain.models.TrackInfo
import com.practicum.playlistmaker.player.ui.TrackFragmentArgs
import com.practicum.playlistmaker.playlist.ui.fragments.PlaylistUpdateFragmentArgs
import com.practicum.playlistmaker.search.ui.adapter.TrackListAdapter.TrackViewHolder.Companion.IMG_RADIUS
import com.practicum.playlistmaker.utils.dpToPx
import org.koin.core.parameter.parametersOf

class PlaylistDetailFragment : Fragment() {

    private var _binding: FragmentPlaylistDetailBinding? = null
    private val binding get() = _binding!!

    private var playlistId: Long = -1
    private var trackId: Long = -1

    private var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>? = null

    private val viewModel: PlaylistDetailViewModel by viewModel {
        parametersOf(playlistId)
    }

    private val arg: PlaylistDetailFragmentArgs by navArgs()

    private lateinit var playlistDetailAdapter: PlaylistDetailTracksAdapter


    private val removeTrackConfirmDialog by lazy {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.playlist_detail_title_delete_track)
            .setMessage(R.string.playlist_detail_message_delete_track)
            .setPositiveButton(R.string.playlist_detail_delete_track) { _, _ ->
                if (trackId != -1L) {
                    viewModel.deleteTrack(
                        trackId
                    )
                }
            }
            .setNegativeButton(R.string.playlist_message_cancel) { _, _ -> trackId = -1 }
    }

    private val removePlaylistConfirmDialog by lazy {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.playlist_detail_title_delete_playlist)
            .setMessage(requireContext().getString(R.string.playlist_detail_message_delete_playlist, viewModel.playlistTitle))
            .setPositiveButton(R.string.playlist_detail_delete_yes) { _, _ ->
                if (playlistId != -1L) {
                    viewModel.deletePlaylist(playlistId = playlistId)
                }
            }
            .setNegativeButton(R.string.playlist_detail_delete_no) { _, _ -> trackId = -1 }
    }

    private val emptyTracksDialog by lazy {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.playlist_detail_delete_empty_tracks)
            .setNeutralButton(R.string.alert_button_ok) { _, _ -> }
    }

    private fun handleBottomSheet(bottomSheetBehavior: BottomSheetBehavior<LinearLayout>?) {
        binding.more.setOnClickListener {
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        bottomSheetBehavior?.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        //viewModel.getPlaylistItems()
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
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetMenu)
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        handleBottomSheet(bottomSheetBehavior)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun share() {
        if (playlistDetailAdapter.playlistTracks.isEmpty()) {
            emptyTracksDialog.show()
        } else {
            viewModel.sharingPlaylist(tracks = playlistDetailAdapter.playlistTracks)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistId = arg.playlistId

        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.shareBtn.setOnClickListener {
            share()
        }

        binding.share.setOnClickListener {
            share()
        }

        binding.editBtn.setOnClickListener {
            if (playlistId != -1L) {
                findNavController().navigate(R.id.action_playlistDetailFragment_to_playlistUpdateFragment, PlaylistUpdateFragmentArgs(playlistId).toBundle())
            }
        }

        binding.removeBtn.setOnClickListener {
            removePlaylistConfirmDialog.show()
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        }

        viewModel.getScreenStateLiveData().observe(viewLifecycleOwner) { playlistDetailState ->
            when (playlistDetailState) {
                is PlaylistDetailState.Loading -> {
                    showLoading()
                }

                is PlaylistDetailState.PlaylistDetailContent -> {
                    showPlaylistDetailInfo(playlist = playlistDetailState.data)
                    showPlaylistDetailInfoBottomSheet(playlist = playlistDetailState.data)
                }

                is PlaylistDetailState.Idle -> {}
            }
        }

        viewModel.isDeleteTrackLiveData().observe(viewLifecycleOwner) { isDelete ->
            if (isDelete) {
                updateAdapter(trackId = trackId)
                updatePlaylistInfo()
                trackId = -1
            }
        }

        viewModel.isDeletePlaylistLiveData().observe(viewLifecycleOwner) { isDelete ->
            if (isDelete) {
                findNavController().navigateUp()
            }
        }

        initBottomSheet()
        initRecyclerView()
    }

    private fun updatePlaylistInfo() {
        val totalDuration = viewModel.getTotalDuration(playlistDetailAdapter.playlistTracks)
        binding.playlistTimeTotal.text = getTimeWordForm(ms = totalDuration)
        binding.playlistCountTotal.text = getWordForm(playlistDetailAdapter.playlistTracks.size)
        if (playlistDetailAdapter.playlistTracks.isEmpty()) {
            showEmptyTrackList()
        }
    }

    private fun updateAdapter(trackId: Long) {
        var position = -1
        playlistDetailAdapter.playlistTracks.forEachIndexed { index, track ->
            if (track.trackId == trackId) {
                position = index
            }
        }
        if (position > -1) {
            playlistDetailAdapter.playlistTracks.removeAt(position)
            playlistDetailAdapter.notifyItemRemoved(position)
            playlistDetailAdapter.notifyItemRangeChanged(
                position,
                playlistDetailAdapter.itemCount - position
            )
        }
    }

    private fun showLoading() {
        binding.progressBar.show()
        binding.mainContainer.gone()
        binding.bottomSheet.gone()
    }

    private fun showContent() {
        binding.progressBar.gone()
        binding.bottomSheet.show()
        binding.mainContainer.show()
    }

    private fun mapToTrackInfo(track: Track): TrackInfo {
        return TrackInfo(
            trackId = track.trackId,
            trackTime = track.getFormatTime(),
            trackName = track.trackName,
            primaryGenreName = track.primaryGenreName,
            collectionName = track.collectionName,
            country = track.country,
            artistName = track.artistName,
            previewUrl = track.previewUrl,
            inFavourite = true,
            releaseDate = track.releaseDate,
            artworkUrl512 = track.getCoverArtwork()
        )
    }

    private fun initRecyclerView() {
        playlistDetailAdapter = PlaylistDetailTracksAdapter(
            object : OnPlaylistTrackListeners {
                override fun onClickHandler(track: Track) {
                    if (viewModel.clickDebounce()) {
                        findNavController().navigate(
                            R.id.action_playlistDetailFragment_to_trackFragment,
                            TrackFragmentArgs(mapToTrackInfo(track)).toBundle()
                        )
                    }
                }

                override fun onLongClickHandler(track: Track) {
                    trackId = track.trackId
                    removeTrackConfirmDialog.show()
                }
            }
        )
        binding.playlistTracks.adapter = playlistDetailAdapter
        binding.playlistTracks.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun addToAdapter(items: List<Track>) {
        playlistDetailAdapter.playlistTracks.addAll(items)
        playlistDetailAdapter.notifyDataSetChanged()
    }

    private fun showEmptyTrackList() {
        binding.playlistContent.gone()
        binding.emptyPlaylist.show()
    }

    private fun showTracks(tracks: List<Track>) {
        if (tracks.isNotEmpty()) {
            addToAdapter(tracks)
        } else {
            showEmptyTrackList()
        }
    }

    private fun showPlaylistDetailInfoBottomSheet(playlist: PlaylistDetail) {
        binding.trackCount.text = getWordForm(playlist.trackList.size)
        binding.posterTitle.text = playlist.title
        Glide.with(requireContext())
            .load(playlist.poster)
            .placeholder(R.drawable.track_placeholder)
            .transform(CenterCrop(), RoundedCorners(dpToPx(IMG_RADIUS, requireContext())))
            .into(binding.posterBottomSheet)
    }

    private fun showPlaylistDetailInfo(playlist: PlaylistDetail) {
        showContent()
        Glide.with(requireContext())
            .load(playlist.poster)
            .placeholder(R.drawable.playlist_detail_empty)
            .centerCrop()
            .into(binding.poster)
        binding.playlistTitle.text = playlist.title
        binding.playlistTimeTotal.text = getTimeWordForm(ms = playlist.duration)
        binding.playlistCountTotal.text = getWordForm(playlist.trackList.size)
        if (playlist.description.isNotEmpty()) {
            binding.playlistDescription.show()
            binding.playlistDescription.text = playlist.description
        } else {
            binding.playlistDescription.gone()
        }
        showTracks(tracks = playlist.trackList)

    }

    override fun onDestroy() {
        _binding = null
        bottomSheetBehavior = null
        super.onDestroy()
    }
}