package com.practicum.playlistmaker.media.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ItemTrackBinding
import com.practicum.playlistmaker.media.ui.adapter.common.OnFavouriteItemClickListener
import com.practicum.playlistmaker.media.ui.models.Track
import com.practicum.playlistmaker.search.ui.adapter.TrackListAdapter.TrackViewHolder.Companion.IMG_RADIUS
import com.practicum.playlistmaker.utils.dpToPx

class PlaylistDetailTracksAdapter(
    private val playlistTrackItemHandler: OnFavouriteItemClickListener
) : RecyclerView.Adapter<PlaylistDetailTracksAdapter.PlaylistDetailViewHolder>() {

    var playlistTracks = mutableListOf<Track>()

    override fun getItemCount(): Int = playlistTracks.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistDetailViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PlaylistDetailViewHolder(ItemTrackBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: PlaylistDetailViewHolder, position: Int) {
        holder.bind(track = playlistTracks[position])
    }

    class PlaylistDetailViewHolder(private val binding: ItemTrackBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(track: Track) {
            binding.trackName.text = track.trackName
            Glide.with(itemView.context)
                .load(track.artworkUrl100)
                .placeholder(R.drawable.track_placeholder)
                .fitCenter()
                .transform(RoundedCorners(dpToPx(IMG_RADIUS, itemView.context)))
                .into(binding.albumPicture)

            binding.trackName.text = track.trackName
            binding.musicalArtistName.text = track.artistName
            binding.duration.text = track.getFormatTime()
            binding.musicalArtistName.requestLayout()
        }
    }
}