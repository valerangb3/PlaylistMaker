package com.practicum.playlistmaker.media.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.ItemTrackBinding
import com.practicum.playlistmaker.media.ui.adapter.common.OnFavouriteItemClickListener

class PlaylistDetailTracksAdapter(
    private val playlistTrackItemHandler: OnFavouriteItemClickListener
) : RecyclerView.Adapter<PlaylistDetailTracksAdapter.PlaylistDetailViewHolder>() {

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistDetailViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: PlaylistDetailViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    class PlaylistDetailViewHolder(private val binding: ItemTrackBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}