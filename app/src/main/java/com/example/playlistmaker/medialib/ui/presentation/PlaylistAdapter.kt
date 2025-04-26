package com.example.playlistmaker.medialib.ui.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.medialib.domain.model.Playlist

class PlaylistAdapter: RecyclerView.Adapter<PlaylistViewHolder>() {

    var playlists: List<Playlist> = emptyList()
        //данные изменились
        set(value) {
            //value - новое значение, присв внутр полю playlists
            field = value
            notifyDataSetChanged()
        }

    private var onPlaylistClick: (Playlist) -> Unit = {}

    fun setOnPlaylistClickListener(listener: (Playlist) -> Unit) {
        onPlaylistClick = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        return PlaylistViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.playlist_view,parent,false)
        )
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener { onPlaylistClick(playlists[position]) }
    }

}