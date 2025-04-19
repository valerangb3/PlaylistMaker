package com.practicum.playlistmaker.media.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistDetailBinding
import com.practicum.playlistmaker.media.presentation.state.PlaylistDetailState
import com.practicum.playlistmaker.media.presentation.viewmodel.PlaylistDetailViewModel
import com.practicum.playlistmaker.media.ui.adapter.PlaylistDetailTracksAdapter
import com.practicum.playlistmaker.media.ui.models.PlaylistDetail
import com.practicum.playlistmaker.utils.getTimeWordForm
import com.practicum.playlistmaker.utils.getWordForm
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.practicum.playlistmaker.utils.gone
import com.practicum.playlistmaker.utils.show
import com.practicum.playlistmaker.media.ui.models.Track

class PlaylistDetailFragment : Fragment() {

    private var _binding: FragmentPlaylistDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlaylistDetailViewModel by viewModel()

    private val arg: PlaylistDetailFragmentArgs by navArgs()

    private lateinit var playlistDetailAdapter: PlaylistDetailTracksAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playlistId = arg.playlistId

        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.getPlaylistDetail(playlistId = playlistId)
        viewModel.getScreenStateLiveData().observe(viewLifecycleOwner) { playlistDetailState ->
            when (playlistDetailState) {
                is PlaylistDetailState.Loading -> { showLoading() }
                is PlaylistDetailState.PlaylistDetailContent -> { showPlaylistDetailInfo(playlistDetailState.data) }
                is PlaylistDetailState.Idle -> {}
            }
        }

        initRecyclerView()
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

    private fun initRecyclerView() {
        playlistDetailAdapter = PlaylistDetailTracksAdapter {

        }
        binding.playlistTracks.adapter = playlistDetailAdapter
        binding.playlistTracks.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun addToAdapter(items: List<Track>) {
        playlistDetailAdapter.playlistTracks.addAll(items)
        playlistDetailAdapter.notifyDataSetChanged()
    }

    private fun showEmptyTrackList() {

    }

    private fun showTracks(tracks: List<Track>) {
        if (tracks.isNotEmpty()) {
            addToAdapter(tracks)
        } else {
            binding.playlistContent.gone()
            binding.emptyPlaylist.show()
        }
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
        super.onDestroy()
    }
}