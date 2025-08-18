package com.practicum.playlistmaker.media.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentMediaBinding
import com.practicum.playlistmaker.media.presentation.viewmodel.FavoriteViewModel
import com.practicum.playlistmaker.ui.App
import com.practicum.playlistmaker.ui.data.model.Screen
import org.koin.androidx.compose.koinViewModel

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
                }
            )
        }
    }

}