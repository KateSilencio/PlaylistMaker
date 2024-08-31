package com.example.playlistmaker

import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.IntentCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.adapter.dpToPx
import com.example.playlistmaker.data.TracksFields
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class MediaActivity : AppCompatActivity() {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3

        private const val ZERO_SEC = "00:00"
        private const val DELAY_SEC = 300L
    }

    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()

    //private lateinit var track: TracksFields
    private lateinit var play: ImageView
    private lateinit var time: TextView
    private lateinit var urlMedia: String

    //потоки
    private lateinit var runnable: Runnable
    val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_media)

        val albumCover = findViewById<ImageView>(R.id.album_cover_media)
        val trackName = findViewById<TextView>(R.id.track_name_media)
        val artistName = findViewById<TextView>(R.id.artist_name_media)
        time = findViewById(R.id.time_media)
        val duration = findViewById<TextView>(R.id.duration_value_media)
        val albumName = findViewById<TextView>(R.id.albom_value_media)
        val year = findViewById<TextView>(R.id.year_value_media)
        val genre = findViewById<TextView>(R.id.genre_value_media)
        val country = findViewById<TextView>(R.id.country_value_media)

        play = findViewById(R.id.play_track_media)

        //track = intent.getParcelableExtra<TracksFields>("TRACK") ?:
        //  throw IllegalArgumentException("Track data not found")

        IntentCompat.getParcelableExtra(intent, "TRACK", TracksFields::class.java)?.let {
            trackName.text = it.trackName
            artistName.text = it.artistName
            ZERO_SEC.also { time.text = it }
            duration.text = SimpleDateFormat("mm:ss", Locale.getDefault())
                .format(it.trackTimeMillis)
            albumName.text = it.collectionName
            year.text = ZonedDateTime.parse(
                it.releaseDate,
                DateTimeFormatter.ISO_ZONED_DATE_TIME
            ).year.toString()
            genre.text = it.primaryGenreName
            country.text = it.country
            urlMedia = it.previewUrl

            val artworkUrl512 = it.getCoverArtwork()

            Glide.with(albumCover.context)
                .load(artworkUrl512)
                .centerCrop()
                .transform(RoundedCorners(albumCover.context.dpToPx(2f)))
                .placeholder(R.drawable.ic_placeholder)
                .into(albumCover)
        }

        //Активация тулбара для окна Медиа
        if (toolbar != null) {
            setSupportActionBar(toolbar)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        runnable = object : Runnable {
            override fun run() {
                if (playerState == STATE_PLAYING) {
                    val currentPosition = mediaPlayer.currentPosition
                    time.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentPosition)
                    handler.postDelayed(this, DELAY_SEC)
                }
            }
        }

        preparePlayer()
        play.setOnClickListener{
            playbackControl()
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(urlMedia)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            play.isEnabled = true
            playerState = STATE_PREPARED
        }

        //отследить завершение воспроизведения
        mediaPlayer.setOnCompletionListener {

            ZERO_SEC.also { time.text = it }
            handler.removeCallbacks(runnable)
            play.setImageResource(R.drawable.ic_play)
            playerState = STATE_PREPARED
        }
    }

    //изменение состояний плеера
    private fun startPlayer(){
        play.setImageResource(R.drawable.ic_pause)
        mediaPlayer.start()
        handler.post(runnable)
        playerState = STATE_PLAYING
    }
    private fun pausePlayer(){
        play.setImageResource(R.drawable.ic_play)
        mediaPlayer.pause()
        handler.removeCallbacks(runnable)
        playerState = STATE_PAUSED
    }

    private fun playbackControl(){
        when(playerState){
            STATE_PLAYING->{
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED->{
                startPlayer()
            }
        }
    }
    //Обработка кнопки Назад
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return true
    }
}