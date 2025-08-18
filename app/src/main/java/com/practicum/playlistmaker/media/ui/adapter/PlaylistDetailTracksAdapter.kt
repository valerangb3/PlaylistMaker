package com.practicum.playlistmaker.media.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil3.ImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.load
import coil3.memory.MemoryCache
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.placeholder
import coil3.request.target
import coil3.request.transformations
import coil3.size.Scale
import coil3.transform.CircleCropTransformation
import coil3.transform.RoundedCornersTransformation
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ItemTrackBinding
import com.practicum.playlistmaker.media.ui.adapter.common.OnPlaylistTrackListeners
import com.practicum.playlistmaker.media.ui.models.Track
import com.practicum.playlistmaker.search.ui.adapter.TrackListAdapter.TrackViewHolder.Companion.IMG_RADIUS
import com.practicum.playlistmaker.utils.dpToPx

class PlaylistDetailTracksAdapter(
    private val playlistTrackItemHandler: OnPlaylistTrackListeners
) : RecyclerView.Adapter<PlaylistDetailTracksAdapter.PlaylistDetailViewHolder>() {

    var playlistTracks = mutableListOf<Track>()

    override fun getItemCount(): Int = playlistTracks.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistDetailViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PlaylistDetailViewHolder(ItemTrackBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: PlaylistDetailViewHolder, position: Int) {
        holder.bind(track = playlistTracks[position])
        holder.itemView.setOnClickListener { playlistTrackItemHandler.onClickHandler(playlistTracks[position]) }
        holder.itemView.setOnLongClickListener {
            playlistTrackItemHandler.onLongClickHandler(playlistTracks[position])
            return@setOnLongClickListener true
        }
    }

    class PlaylistDetailViewHolder(private val binding: ItemTrackBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(track: Track) {
            binding.trackName.text = track.trackName

            val imageLoader = ImageLoader.Builder(itemView.context)
                .memoryCache {
                    MemoryCache.Builder()
                        .maxSizePercent(itemView.context, 0.25)
                        .build()
                }
                .diskCache {
                    DiskCache.Builder()
                        .directory(itemView.context.cacheDir.resolve("image_cache"))
                        .maxSizePercent(0.02)
                        .build()
                }
                .build()

            val request = ImageRequest.Builder(itemView.context)
                .data("https://picsum.photos/200")
                .crossfade(true)
                .target(binding.albumPicture)
                .build()
            imageLoader.enqueue(request)

            /*Glide.with(itemView.context)
                .load(track.artworkUrl100)
                .placeholder(R.drawable.track_placeholder)
                .fitCenter()
                .transform(RoundedCorners(dpToPx(IMG_RADIUS, itemView.context)))
                .into(binding.albumPicture)*/

            binding.trackName.text = track.trackName
            binding.musicalArtistName.text = track.artistName
            binding.duration.text = track.getFormatTime()
            binding.musicalArtistName.requestLayout()
        }
    }
}