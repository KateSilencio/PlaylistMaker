package com.example.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.player.domain.models.TracksParceling
import com.example.playlistmaker.player.ui.MediaActivity

class TracksAdapter(
    private var tracks: MutableList<TracksParceling>
) : RecyclerView.Adapter<TracksViewHolder>() {

    //Handler and Debounce
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private var onTrackClick: (TracksParceling) -> Unit = {}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        return TracksViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.track_view, parent, false)
        )
    }

    fun setOnClickListener(onClickListener: (TracksParceling) -> Unit) {
        onTrackClick = onClickListener
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            if (clickDebounce()){
                onTrackClick(tracks[position])

                //для перехода в MediaActivity
                val context = holder.itemView.context
                val intent = Intent(context, MediaActivity::class.java)
                intent.putExtra("TRACK", tracks[position])
                context.startActivity(intent)
            }
        }
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateTracks(newTracks: List<TracksParceling>) {
        tracks.clear()
        tracks.addAll(newTracks)
        notifyDataSetChanged()
    }
}
