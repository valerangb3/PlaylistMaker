package com.practicum.playlistmaker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.placeholder
import coil3.request.target
import coil3.request.transformations
import coil3.size.Scale
import coil3.transform.RoundedCornersTransformation
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ItemPlaylistPlayerBinding
import com.practicum.playlistmaker.player.ui.models.PlaylistTrack
import com.practicum.playlistmaker.utils.dpToPx
import com.practicum.playlistmaker.utils.getWordForm

class PlaylistAdapter(
    private val playlistItemHandler: OnItemClickListener
) : RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    companion object {
        const val IMG_RADIUS = 2.0F
    }
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

            ImageLoader(binding.poster.context).enqueue(
                ImageRequest.Builder(binding.poster.context)
                    .data(playlist.posterPath)
                    .placeholder(R.drawable.track_placeholder)
                    .crossfade(true)
                    .scale(Scale.FIT)
                    .transformations(RoundedCornersTransformation(radius = dpToPx(IMG_RADIUS, binding.poster.context).toFloat()))
                    .target(binding.poster)
                    .build()
            )
        }

    }
}