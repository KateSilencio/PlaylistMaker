package com.example.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.player.domain.models.TracksData

class TracksAdapter(
    private var tracks: MutableList<TracksData>
) : RecyclerView.Adapter<TracksViewHolder>() {


/*    //Handler and Debounce
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val TRACK_KEY = "TRACK"
    }*/

    private var onTrackClick: (TracksData) -> Unit = {}
    //долгое нажатие
    var onLongClickListener: (TracksData) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        return TracksViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.track_view, parent, false)
        )
    }

    fun setOnClickListener(onClickListener: (TracksData) -> Unit) {
        onTrackClick = onClickListener
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
//            if (clickDebounce()){
//                onTrackClick(tracks[position])
//
//                //для перехода в MediaActivity
//                val context = holder.itemView.context
//                val intent = Intent(context, MediaActivity::class.java)
//                intent.putExtra(TRACK_KEY, tracks[position])
//                context.startActivity(intent)
//            }
            onTrackClick(tracks[position])
        }
        //долгое нажатие
        holder.itemView.setOnLongClickListener {
            onLongClickListener(tracks[position])
            true
        }
    }

//    private fun clickDebounce() : Boolean {
//        val current = isClickAllowed
//        if (isClickAllowed) {
//            isClickAllowed = false
//            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
//        }
//        return current
//    }

    fun updateTracks(newTracks: List<TracksData>) {
        tracks.clear()
        tracks.addAll(newTracks)
        notifyDataSetChanged()
    }
}
