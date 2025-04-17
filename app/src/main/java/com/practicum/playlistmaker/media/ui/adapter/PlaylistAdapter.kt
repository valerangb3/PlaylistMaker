package com.practicum.playlistmaker.media.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ItemPlaylistMediaBinding
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.media.ui.adapter.common.OnPlaylistItemClickListener
import com.practicum.playlistmaker.utils.dpToPx

class PlaylistAdapter(
    private val playlistItemHandler: OnPlaylistItemClickListener
) : RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    companion object {
        const val IMG_RADIUS = 8.0F
    }

    var playlist = mutableListOf<Playlist>()


    override fun getItemCount(): Int = playlist.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistViewHolder(ItemPlaylistMediaBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlist[position])
        holder.itemView.setOnClickListener { playlistItemHandler.onItemClick(playlistId = playlist[position].id) }
    }

    class PlaylistViewHolder(private val binding: ItemPlaylistMediaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(playList: Playlist) {

            Glide.with(itemView.context)
                .load(playList.filePath)
                .placeholder(R.drawable.track_placeholder)
                .transform(
                    CenterCrop(),
                    RoundedCorners(dpToPx(IMG_RADIUS, itemView.context))
                )
                .into(binding.poster)

            binding.posterTitle.text = playList.title
            binding.trackCount.text = itemView.context.resources.getQuantityString(R.plurals.playlist_track_count, playList.count, playList.count)
        }

    }

}