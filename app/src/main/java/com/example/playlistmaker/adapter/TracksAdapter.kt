package com.example.playlistmaker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.data.ResultFields

class TracksAdapter(private val tracks: List<ResultFields>):    //List<Track>
                    RecyclerView.Adapter<TracksViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        return TracksViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.track_view,parent,false))
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(tracks[position])
    }
}