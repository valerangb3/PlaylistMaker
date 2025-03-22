package com.practicum.playlistmaker.media.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentFavoritesBinding
import com.practicum.playlistmaker.media.domain.models.Favourite
import com.practicum.playlistmaker.media.presentation.state.FavoriteState
import com.practicum.playlistmaker.media.presentation.viewmodel.FavoriteViewModel
import com.practicum.playlistmaker.media.ui.adapter.FavouriteListAdapter
import com.practicum.playlistmaker.player.domain.models.TrackInfo
import com.practicum.playlistmaker.player.ui.TrackActivityArgs
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.SearchFragment
import com.practicum.playlistmaker.utils.gone
import com.practicum.playlistmaker.utils.show
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private var _binding : FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private var isClickAllowed = true

    private lateinit var favouriteListAdapter: FavouriteListAdapter

    private val viewModel: FavoriteViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initRecyclerView()

        viewModel.getFavouriteList()
        viewModel.getScreenStateLiveData().observe(requireActivity()) { screenState ->
            when (screenState) {
                is FavoriteState.EmptyContent -> showEmptyContent(getString(screenState.res))
                is FavoriteState.FavouriteContent -> showContent(screenState.data)
                is FavoriteState.Loading -> showLoading()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun mapToTrackInfo(favourite: Favourite): TrackInfo {
        return TrackInfo(
            trackId = favourite.trackId,
            trackTime = favourite.trackTime,
            trackName = favourite.trackName,
            primaryGenreName = favourite.primaryGenreName,
            collectionName = favourite.collectionName,
            country = favourite.country,
            artistName = favourite.artistName,
            previewUrl = favourite.previewUrl,
            inFavourite = true,
            releaseDate = favourite.releaseDate,
            artworkUrl512 = favourite.getCoverArtwork()
        )
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    private fun handleTap(favouriteItem: Favourite) {
        findNavController().navigate(R.id.action_mediaFragment_to_trackActivity, TrackActivityArgs(mapToTrackInfo(favouriteItem)).toBundle())
    }

    private fun initRecyclerView() {
        favouriteListAdapter = FavouriteListAdapter {
            if (clickDebounce()) {
                handleTap(it)
            }
        }
        binding.favouriteList.adapter = favouriteListAdapter
        binding.favouriteList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun clearListAdapter() {
        if (favouriteListAdapter.favouriteList.isNotEmpty()) {
            favouriteListAdapter.favouriteList.clear()
            favouriteListAdapter.notifyDataSetChanged()
        }
    }

    private fun addToAdapter(items: List<Favourite>) {
        favouriteListAdapter.favouriteList.addAll(items)
        favouriteListAdapter.notifyDataSetChanged()
    }

    private fun hideErrorContainer() {
        binding.errorContainer.gone()
    }

    private fun showContent(favouriteList: List<Favourite>) {
        hideErrorContainer()
        hideLoading()
        clearListAdapter()
        addToAdapter(favouriteList)
    }

    private fun hideLoading() {
        binding.progressBar.gone()
    }

    private fun showLoading() {
        binding.progressBar.show()
    }

    private fun showEmptyContent(text: String) {
        hideLoading()
        clearListAdapter()
        binding.errorContainer.show()
        binding.errorText.text = text
    }

    companion object {
        fun newInstance() = FavoritesFragment()
        const val CLICK_DEBOUNCE_DELAY = 1_000L
    }
}