package com.practicum.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import com.practicum.playlistmaker.ui.App
import com.practicum.playlistmaker.ui.data.model.Screen

class SettingsFragment : Fragment() {
    private var _binding : FragmentSettingsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.settingsScreen.setContent {
            App(
                screen = Screen.SETTINGS,
                onSearchItem = {
                    findNavController().navigate(R.id.searchFragment)
                },
                onMediaItem = {
                    findNavController().navigate(R.id.mediaFragment)
                },
            )
        }
    }
}