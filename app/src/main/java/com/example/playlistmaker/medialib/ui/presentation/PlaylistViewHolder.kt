package com.example.playlistmaker.medialib.ui.presentation

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.medialib.domain.model.Playlist
import com.example.playlistmaker.player.ui.dpToPx

class PlaylistViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val cover: ImageView = itemView.findViewById(R.id.image_cover)
    private val name: TextView = itemView.findViewById(R.id.playlist_name)
    private val count: TextView = itemView.findViewById(R.id.playlist_count)

    fun bind(playlist: Playlist){
        name.text = playlist.title
        count.text = itemView.context.resources.getQuantityString(
            R.plurals.tracks_count,
            playlist.trackCount,
            playlist.trackCount
        )

        if (playlist.coverUri != null) {
            Glide.with(itemView)
                .load(playlist.coverUri)
                .transform(RoundedCorners(itemView.context.dpToPx(8f)))
                .into(cover)
        } else {
            cover.setImageResource(R.drawable.ic_placeholder)
        }
    }

}