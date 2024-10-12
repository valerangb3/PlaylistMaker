package com.practicum.playlistmaker

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.data.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackListAdapter(
    val trackList: MutableList<Track>
) : RecyclerView.Adapter<TrackListAdapter.TrackViewHolder>() {

    override fun getItemCount(): Int = trackList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder = TrackViewHolder(parent)

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
    }

    class TrackViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
    ) {
        private val albumPictureView: ImageView = itemView.findViewById(R.id.album_picture)
        private val trackView: TextView = itemView.findViewById(R.id.track_name)
        private val musicalView: TextView = itemView.findViewById(R.id.musical_artist_name)
        private val directionView: TextView = itemView.findViewById(R.id.duration)

        fun bind(trackItem: Track) {
            Glide.with(itemView.context)
                .load(trackItem.artworkUrl100)
                .placeholder(R.drawable.track_placeholder)
                .fitCenter()
                .transform(RoundedCorners(dpToPx(IMG_RADIUS, itemView.context)))
                .into(albumPictureView)

            trackView.text = trackItem.trackName
            musicalView.text = trackItem.artistName
            directionView.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackItem.trackTimeMillis)
            musicalView.requestLayout()

        }

        private fun dpToPx(dp: Float, context: Context): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.resources.displayMetrics
            ).toInt()
        }

        companion object {
            const val IMG_RADIUS = 2.0F
        }
    }
}