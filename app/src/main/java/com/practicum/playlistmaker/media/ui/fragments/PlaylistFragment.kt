package com.practicum.playlistmaker.media.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.media.domain.models.Playlist as PlaylistMedia
import com.practicum.playlistmaker.playlist.presentation.models.Playlist
import com.practicum.playlistmaker.media.presentation.state.PlaylistState
import com.practicum.playlistmaker.media.presentation.viewmodel.PlaylistViewModel
import com.practicum.playlistmaker.media.ui.adapter.PlaylistAdapter
import com.practicum.playlistmaker.playlist.ui.fragments.PlaylistMakerFragment
import com.practicum.playlistmaker.utils.gone
import com.practicum.playlistmaker.utils.show
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlaylistViewModel by viewModel()

    private lateinit var playlistAdapter: PlaylistAdapter

    override fun onResume() {
        super.onResume()
        Log.d("SPRINT22", "onCreate")
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        val navBackStackEntry = findNavController().currentBackStackEntry
        navBackStackEntry?.savedStateHandle?.getLiveData<Playlist>(PlaylistMakerFragment.NEW_PLAYLIST_RESULT)
            ?.observe(
                viewLifecycleOwner
            ) { playlistItem ->
                viewModel.addPlaylist(PlaylistMedia(
                    filePath = playlistItem.fileUri,
                    title = playlistItem.title,
                    count = 0
                ))
            }

        initRecyclerView()
        viewModel.getScreenStateLiveData().observe(requireActivity()) { screenState ->
            when (screenState) {
                is PlaylistState.EmptyContent -> showEmptyContent(getString(screenState.res))
                is PlaylistState.Loading -> showLoading()
                is PlaylistState.PlaylistContent -> showContent(screenState.data)
            }
        }

        binding.create.setOnClickListener {
            navToPlayListMaker()
        }
    }

    private fun hideLoading() {
        binding.progressBar.gone()
    }

    private fun clearListAdapter() {
        if (playlistAdapter.playlist.isNotEmpty()) {
            playlistAdapter.playlist.clear()
            playlistAdapter.notifyDataSetChanged()
        }
    }

    private fun initRecyclerView() {
        playlistAdapter = PlaylistAdapter()
        binding.playlistItems.adapter = playlistAdapter
        binding.playlistItems.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    private fun navToPlayListMaker() {
        findNavController().navigate(R.id.action_mediaFragment_to_playlistMaker)
    }

    private fun showContent(playlistItems: List<PlaylistMedia>) {
        hideErrorContainer()
        hideLoading()
        clearListAdapter()
        addToAdapter(playlistItems)
        binding.playlistItems.show()
        binding.create.show()
    }

    private fun addToAdapter(items: List<PlaylistMedia>) {
        playlistAdapter.playlist.addAll(items)
        playlistAdapter.notifyDataSetChanged()
    }

    private fun showLoading() {
        binding.create.gone()
        hideErrorContainer()
        binding.playlistItems.gone()
        binding.progressBar.show()
    }

    private fun hideErrorContainer() {
        binding.errorContainer.gone()
    }

    private fun showEmptyContent(text: String) {
        hideLoading()
        clearListAdapter()
        binding.playlistItems.gone()
        binding.create.show()
        binding.errorContainer.show()
        binding.errorText.text = text
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance() = PlaylistFragment()
    }
}