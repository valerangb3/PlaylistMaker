package com.practicum.playlistmaker.search.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ItemTrackBinding
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.adapter.common.OnItemClickListener
import com.practicum.playlistmaker.utils.dpToPx

class TrackListAdapter(
    private val trackItemHandler: OnItemClickListener
) : RecyclerView.Adapter<TrackListAdapter.TrackViewHolder>() {

    var trackList = mutableListOf<Track>()

    override fun getItemCount(): Int = trackList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return TrackViewHolder(ItemTrackBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener { trackItemHandler.onItemClick(trackList[position]) }
    }

    class TrackViewHolder(private val binding: ItemTrackBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {

        fun bind(trackItem: Track) {
            Glide.with(itemView.context)
                .load(trackItem.artworkUrl100)
                .placeholder(R.drawable.track_placeholder)
                .fitCenter()
                .transform(RoundedCorners(dpToPx(IMG_RADIUS, itemView.context)))
                .into(binding.albumPicture)

            binding.trackName.text = trackItem.trackName
            binding.musicalArtistName.text = trackItem.artistName
            binding.duration.text = trackItem.trackTime
            binding.musicalArtistName.requestLayout()

        }

        companion object {
            const val IMG_RADIUS = 2.0F
        }
    }
}