package com.example.playlistmaker.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.MediaActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.data.TracksFields

class TracksAdapter(
    private val tracks: List<TracksFields>
) : RecyclerView.Adapter<TracksViewHolder>() {


    private var onTrackClick: (TracksFields) -> Unit = {}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        return TracksViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.track_view, parent, false)
        )
    }

    fun setOnClickListener(onClickListener: (TracksFields) -> Unit) {
        onTrackClick = onClickListener
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            onTrackClick(tracks[position])

            //для перехода в MediaActivity
            val context = holder.itemView.context
            val intent = Intent(context, MediaActivity::class.java)
            intent.putExtra("TRACK", tracks[position])
            context.startActivity(intent)
        }
    }
}
