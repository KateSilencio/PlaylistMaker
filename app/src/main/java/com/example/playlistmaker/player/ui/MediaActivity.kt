package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.domain.mapper.TrackMapper
import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.player.domain.models.TracksData
import com.example.playlistmaker.player.ui.presenter.MediaViewModel
import com.example.playlistmaker.player.ui.presenter.MediaViewModelFactory
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class MediaActivity : AppCompatActivity() {

    companion object {
        private const val TRACK_KEY = "TRACK"
    }

    private lateinit var mediaViewModel: MediaViewModel
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    private var albumCover: ImageView? = null
    private var trackName: TextView? = null
    private var artistName: TextView? = null
    private var duration: TextView? = null
    private var albumName: TextView? = null
    private var year: TextView? = null
    private var genre: TextView? = null
    private var play: ImageView? = null
    private var time: TextView? = null
    private var country: TextView? = null

    private var urlMedia: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)
        Creator.initialize(this)

        albumCover = findViewById(R.id.album_cover_media)
        trackName = findViewById(R.id.track_name_media)
        artistName = findViewById(R.id.artist_name_media)
        time = findViewById(R.id.time_media)
        duration = findViewById(R.id.duration_value_media)
        albumName = findViewById(R.id.albom_value_media)
        year = findViewById(R.id.year_value_media)
        genre = findViewById(R.id.genre_value_media)
        country = findViewById(R.id.country_value_media)
        play = findViewById(R.id.play_track_media)

        mediaViewModel = ViewModelProvider(
            this,
            MediaViewModelFactory(
                Creator.provideMediaPlayerInteractor()
            )
        )[MediaViewModel::class.java]

        //Активация тулбара для окна Медиа
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_media)
        if (toolbar != null) {
            setSupportActionBar(toolbar)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        IntentCompat.getParcelableExtra(intent, TRACK_KEY, TracksData::class.java)?.let {
            val track = TrackMapper.convertToTrack(it)
            mediaViewModel.preparePlayer(track)
        }

        mediaViewModel.mediaStateLive.observe(this, Observer { state ->
            state.track?.let {
                showDetailsTrack(it)
                play?.setImageResource(
                    if (state.isPlaying)
                        R.drawable.ic_pause
                    else
                        R.drawable.ic_play
                )
                time?.text = state.currentTime
            }
        })
        play?.setOnClickListener {
            mediaViewModel.playbackControl()
        }
    }

    private fun showDetailsTrack(track: Track) {
        trackName?.text = track.trackName
        artistName?.text = track.artistName
        time?.text = dateFormat.format(0)
        duration?.text = dateFormat.format(track.trackTimeMillis)
        albumName?.text = track.collectionName
        year?.text = ZonedDateTime.parse(
            track.releaseDate,
            DateTimeFormatter.ISO_ZONED_DATE_TIME
        ).year.toString()
        genre?.text = track.primaryGenreName
        country?.text = track.country
        urlMedia = track.previewUrl

        Glide.with(albumCover!!.context)
            .load(track.artworkUrl)
            .centerCrop()
            .transform(RoundedCorners(albumCover!!.context.dpToPx(2f)))
            .placeholder(R.drawable.ic_placeholder)
            .into(albumCover!!)
    }

    //Обработка кнопки Назад
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return true
    }

    override fun onPause() {
        super.onPause()
        mediaViewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaViewModel.releaseMedia()
    }
}