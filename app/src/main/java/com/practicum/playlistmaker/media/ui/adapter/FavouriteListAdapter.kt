package com.practicum.playlistmaker.media.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ItemTrackBinding
import com.practicum.playlistmaker.media.domain.models.Favourite
import com.practicum.playlistmaker.media.ui.adapter.common.OnItemClickListener
import com.practicum.playlistmaker.search.ui.adapter.TrackListAdapter.TrackViewHolder
import com.practicum.playlistmaker.utils.dpToPx

class FavouriteListAdapter(
    private val favouriteItemHandler: OnItemClickListener
) : RecyclerView.Adapter<FavouriteListAdapter.FavouriteViewHolder>() {

    var favouriteList = mutableListOf<Favourite>()

    override fun getItemCount(): Int = favouriteList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return FavouriteViewHolder(ItemTrackBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        holder.bind(favouriteList[position])
        holder.itemView.setOnClickListener {
            favouriteItemHandler.onItemClick(favouriteList[position])
        }
    }

    class FavouriteViewHolder(private val binding: ItemTrackBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(favourite: Favourite) {
            Glide.with(itemView.context)
                .load(favourite.artworkUrl100)
                .placeholder(R.drawable.track_placeholder)
                .fitCenter()
                .transform(RoundedCorners(dpToPx(IMG_RADIUS, itemView.context)))
                .into(binding.albumPicture)

            binding.trackName.text = favourite.trackName
            binding.musicalArtistName.text = favourite.artistName
            binding.duration.text = favourite.trackTime
            binding.musicalArtistName.requestLayout()
        }

        companion object {
            const val IMG_RADIUS = 2.0F
        }
    }
}