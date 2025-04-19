package com.practicum.playlistmaker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ItemPlaylistPlayerBinding
import com.practicum.playlistmaker.player.ui.models.PlaylistTrack
import com.practicum.playlistmaker.search.ui.adapter.TrackListAdapter.TrackViewHolder.Companion.IMG_RADIUS
import com.practicum.playlistmaker.utils.dpToPx
import com.practicum.playlistmaker.utils.getWordForm

class PlaylistAdapter(
    private val playlistItemHandler: OnItemClickListener
) : RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    var playlistItems = mutableListOf<PlaylistTrack>()

    override fun getItemCount(): Int = playlistItems.size

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.binding(playlist = playlistItems[position])
        holder.itemView.setOnClickListener { playlistItemHandler.onItemClick(playlistItems[position]) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PlaylistViewHolder(ItemPlaylistPlayerBinding.inflate(layoutInflater, parent, false))
    }

    class PlaylistViewHolder(private val binding: ItemPlaylistPlayerBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun binding(playlist: PlaylistTrack) {

            binding.trackCount.text = getWordForm(playlist.playlistItemsId.size)
            binding.posterTitle.text = playlist.title
            Glide.with(itemView.context)
                .load(playlist.posterPath)
                .placeholder(R.drawable.track_placeholder)
                .fitCenter()
                .transform(RoundedCorners(dpToPx(IMG_RADIUS, itemView.context)))
                .into(binding.poster)
        }

    }
}