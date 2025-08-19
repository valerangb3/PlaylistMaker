package com.practicum.playlistmaker.media.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentMediaBinding
import com.practicum.playlistmaker.player.domain.models.TrackInfo
import com.practicum.playlistmaker.player.ui.TrackFragmentArgs
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.ui.App
import com.practicum.playlistmaker.ui.data.model.Screen

class MediaFragment: Fragment() {

    private var _binding : FragmentMediaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun mapToTrackInfo(track: Track): TrackInfo {
        return TrackInfo(
            trackId = track.trackId,
            trackTime = track.trackTime,
            trackName = track.trackName,
            primaryGenreName = track.primaryGenreName,
            collectionName = track.collectionName,
            country = track.country,
            artistName = track.artistName,
            previewUrl = track.previewUrl,
            inFavourite = track.inFavourite,
            releaseDate = track.releaseDate,
            artworkUrl512 = track.getCoverArtwork()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mediaScreen.setContent {
            App(
                screen = Screen.MEDIA,
                onSearchItem = {
                    findNavController().navigate(R.id.searchFragment)
                },
                onMediaItem = {
                    findNavController().navigate(R.id.mediaFragment)
                },
                onSettingsItem = {
                    findNavController().navigate(R.id.settingsFragment)
                },
                onTrackClick = { track ->
                    findNavController().navigate(R.id.trackFragment, TrackFragmentArgs(mapToTrackInfo(track)).toBundle())
                },
                onPlaylistClick = { playlistId ->
                    findNavController().navigate(R.id.playlistDetailFragment, PlaylistDetailFragmentArgs(playlistId).toBundle())
                },
                handler = {
                    findNavController().navigate(R.id.playlistMakerFragment)
                }
            )
        }
    }

}