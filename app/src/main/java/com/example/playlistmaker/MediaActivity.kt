package com.example.playlistmaker

import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    private lateinit var track: TracksFields
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_media)

        val albumCover = findViewById<ImageView>(R.id.album_cover_media)
        val trackName = findViewById<TextView>(R.id.track_name_media)
        val artistName = findViewById<TextView>(R.id.artist_name_media)
        val time = findViewById<TextView>(R.id.time_media)
        val duration = findViewById<TextView>(R.id.duration_value_media)
        val albumName = findViewById<TextView>(R.id.albom_value_media)
        val year = findViewById<TextView>(R.id.year_value_media)
        val genre = findViewById<TextView>(R.id.genre_value_media)
        val country = findViewById<TextView>(R.id.country_value_media)

        //track = intent.getParcelableExtra<TracksFields>("TRACK") ?:
        //  throw IllegalArgumentException("Track data not found")

        IntentCompat.getParcelableExtra(intent, "TRACK", TracksFields::class.java)?.let {
            trackName.text = it.trackName
            artistName.text = it.artistName
            time.text = SimpleDateFormat("mm:ss", Locale.getDefault())
                .format(it.trackTimeMillis)
            duration.text = time.text
            albumName.text = it.collectionName
            year.text = ZonedDateTime.parse(
                it.releaseDate,
                DateTimeFormatter.ISO_ZONED_DATE_TIME
            ).year.toString()
            genre.text = it.primaryGenreName
            country.text = it.country

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
    }

    //Обработка кнопки Назад
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return true
    }
}