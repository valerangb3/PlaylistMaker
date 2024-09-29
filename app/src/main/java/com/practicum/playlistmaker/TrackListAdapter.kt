package com.practicum.playlistmaker

import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackListAdapter(
    private val trackList: List<Track>
): RecyclerView.Adapter<TrackListAdapter.TrackViewHolder>() {

    override fun getItemCount(): Int = trackList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false))
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        Log.d("SPRINT_10", holder.toString())
        holder.bind(trackList[position])
    }

    class TrackViewHolder(parentView: View): RecyclerView.ViewHolder(parentView) {
        private val albumPictureView: ImageView = parentView.findViewById(R.id.album_picture)
        private val trackView: TextView = parentView.findViewById(R.id.track_name)
        private val musicalView: TextView = parentView.findViewById(R.id.musical_artist_name)
        private val directionView: TextView = parentView.findViewById(R.id.duration)

        fun bind(trackItem: Track) {
            Log.d("SPRINT_10", trackItem.trackName)
            Glide.with(this.itemView.context)
                .load(trackItem.src)
                .placeholder(R.drawable.track_placeholder)
                .fitCenter()
                .transform(RoundedCorners(dpToPx(IMG_RADIUS, this.itemView.context)))
                .into(albumPictureView)

            trackView.text = trackItem.trackName
            musicalView.text = trackItem.singerName
            directionView.text = trackItem.duration
            musicalView.requestLayout()
        }

        private fun dpToPx(dp: Float, context: Context): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.resources.displayMetrics).toInt()
        }

        companion object {
            const val IMG_RADIUS = 2.0F
        }
    }
}