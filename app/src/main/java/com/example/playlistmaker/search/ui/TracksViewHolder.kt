package com.example.playlistmaker.search.ui

import android.icu.text.SimpleDateFormat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.player.domain.models.TracksData
import com.example.playlistmaker.player.ui.dpToPx
import java.util.Locale

class TracksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val trackName: TextView = itemView.requireViewById(R.id.track_name)
    val artistName: TextView = itemView.requireViewById(R.id.artist_name)
    val trackTime: TextView = itemView.requireViewById(R.id.track_time)
    val artworkUrl: ImageView = itemView.findViewById(R.id.image_albom)

    fun bind(track: TracksData) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault())
                                        .format(track.trackTimeMillis)

        Glide.with(artworkUrl.context)
            .load(track.artworkUrl100)
            .centerCrop()
            .transform(RoundedCorners(artworkUrl.context.dpToPx(2f)))
            .placeholder(R.drawable.ic_placeholder)
            .into(artworkUrl)
    }
}