package com.practicum.playlistmaker.search.ui

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.player.domain.models.TrackInfo
import com.practicum.playlistmaker.playlist.ui.fragments.PlaylistUpdateFragmentArgs
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.ui.App
import com.practicum.playlistmaker.ui.data.model.Screen
import com.practicum.playlistmaker.utils.NetworkConnectBroadcastReceiver

class SearchFragment : Fragment() {
    private val receiver = NetworkConnectBroadcastReceiver()

    private var _binding : FragmentSearchBinding? = null
    private val binding get() = _binding!!

    //private val viewModel by viewModel<SearchViewModel>()

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.searchScreen.setContent {
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
                }
            )
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //HERE
    }

    override fun onPause() {
        //viewModel.saveHistory()
        super.onPause()
        requireContext().unregisterReceiver(receiver)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        requireContext().registerReceiver(receiver, filter)
    }
}