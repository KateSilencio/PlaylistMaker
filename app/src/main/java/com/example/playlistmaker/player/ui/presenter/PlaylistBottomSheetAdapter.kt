package com.example.playlistmaker.player.ui.presenter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.medialib.domain.model.Playlist

class PlaylistBottomSheetAdapter(
    private var playlists: List<Playlist>
): RecyclerView.Adapter<PlaylistBottomSheetViewHolder>() {

    var onPlaylistClick: (Playlist) -> Unit = {}
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistBottomSheetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.playlist_bottomsheet_view, parent, false)
        return PlaylistBottomSheetViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistBottomSheetViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener { onPlaylistClick(playlists[position]) }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updatePlaylists(newPlaylists: List<Playlist>) {
        this.playlists = newPlaylists
        notifyDataSetChanged()

        Log.d("AAA", "Data updated PlaylistBottomSheetAdapter: ${newPlaylists.size} items")
    }
}