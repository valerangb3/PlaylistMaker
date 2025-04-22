package com.practicum.playlistmaker.playlist.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlist.presentation.models.Playlist
import com.practicum.playlistmaker.playlist.presentation.state.PlaylistUpdateState
import com.practicum.playlistmaker.playlist.presentation.viewmodel.PlaylistUpdateViewModel
import com.practicum.playlistmaker.utils.dpToPx
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistUpdateFragment : PlaylistMakerFragment() {

    companion object {
        private const val POSTER_RADIUS = 8.0F
    }

    override val viewModel: PlaylistUpdateViewModel by viewModel()

    private val args: PlaylistUpdateFragmentArgs by navArgs()

    private var playlistId: Long = -1

    private fun fillPlaylistData(playlist: Playlist) {
        if (playlist.fileUri.isNotEmpty()) {
            Glide.with(requireContext())
                .load(playlist.fileUri)
                .transform(
                    CenterCrop(),
                    RoundedCorners(dpToPx(POSTER_RADIUS, requireContext()))
                )
                .into(binding.poster)
        }
        binding.editTitle.editText?.setText(playlist.title)
        binding.wrapperDescription.editText?.setText(playlist.description)
    }

    private fun update() {
        binding.createPlaylist.setOnClickListener {
            if (playlistId != -1L && it.isEnabled) {
                val description = binding.wrapperDescription.editText?.text?.toString() ?: ""
                val title = binding.editTitle.editText?.text?.toString() ?: ""
                playlistItem = Playlist(
                    title = title,
                    description = description,
                    fileUri = fileUri
                )
                viewModel.updatePlaylist(
                    playlistId = playlistId,
                    newData = playlistItem
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }

        playlistId = args.playlistId

        backPressedCallback?.let { callback ->
            callback.isEnabled = false
        }

        binding.titleText.text = requireContext().getString(R.string.playlist_update_edit_title)
        binding.createPlaylist.text = requireContext().getString(R.string.playlist_update_button)

        update()

        if (playlistId != -1L) {
            viewModel.getPlaylist(playlistId = playlistId)
        }

        viewModel.getScreenStateUpdateLiveData().observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistUpdateState.Idle -> {}
                is PlaylistUpdateState.PlaylistState -> {
                    fillPlaylistData(playlist = state.playlist)
                }
                is PlaylistUpdateState.Update -> {
                    findNavController().navigateUp()
                }
            }
        }
    }

}