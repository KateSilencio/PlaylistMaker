package com.example.playlistmaker.ui.media

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.api.media.usecase.MediaPlayerUseCase
import com.example.playlistmaker.domain.mapper.TrackMapper
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.TracksParceling
import com.example.playlistmaker.ui.dpToPx
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class MediaActivity : AppCompatActivity() {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3

        private const val DELAY_SEC = 300L
    }

    private var playerState = STATE_DEFAULT

    private val mediaPlayer: MediaPlayerUseCase by lazy {
        Creator.provideMediaPlayerUseCase()
    }

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

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    //потоки
    private lateinit var runnable: Runnable
    val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_media)

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

        IntentCompat.getParcelableExtra(intent, "TRACK", TracksParceling::class.java)?.let {
            val track = TrackMapper.toDomain(it)
            showDetailsTrack(track)
        }

        //Активация тулбара для окна Медиа
        if (toolbar != null) {
            setSupportActionBar(toolbar)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        runnable = object : Runnable {
            override fun run() {
                if (playerState == STATE_PLAYING) {
                    val currentPosition = mediaPlayer.getCurrentPosition()
                    time?.text = dateFormat.format(currentPosition)
                    handler.postDelayed(this, DELAY_SEC)
                }
            }
        }

        preparePlayer()
        play?.setOnClickListener {
            playbackControl()
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.releaseMedia()
    }

    private fun preparePlayer() {
        urlMedia?.let {
            mediaPlayer.prepareMedia(it)
            mediaPlayer.setOnPreparedListener {
                play?.isEnabled = true
                playerState = STATE_PREPARED
            }

            //отследить завершение воспроизведения
            mediaPlayer.setOnCompletionListener {
                time?.text = dateFormat.format(0)
                handler.removeCallbacks(runnable)
                play?.setImageResource(R.drawable.ic_play)
                playerState = STATE_PREPARED
            }
        }
    }

    //изменение состояний плеера
    private fun startPlayer() {
        play?.setImageResource(R.drawable.ic_pause)
        mediaPlayer.startMedia()
        handler.post(runnable)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        play?.setImageResource(R.drawable.ic_play)
        mediaPlayer.pauseMedia()
        handler.removeCallbacks(runnable)
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
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
}