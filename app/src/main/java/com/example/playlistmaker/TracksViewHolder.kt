package com.example.playlistmaker

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TracksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val trackName: TextView = itemView.requireViewById(R.id.track_name)
    val artistName: TextView = itemView.requireViewById(R.id.artist_name)
    val trackTime: TextView = itemView.requireViewById(R.id.track_time)
    val artworkUrl: ImageView = itemView.findViewById(R.id.image_albom)

    fun bind(track: Track) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = track.trackTime

        Glide.with(artworkUrl.context)
            .load(track.artworkUrl100)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(2f, artworkUrl.context)))
            .placeholder(R.drawable.ic_placeholder)
            .into(artworkUrl)
    }

    //конвертация dp в px
    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

}