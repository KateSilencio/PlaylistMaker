package com.example.playlistmaker.medialib.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_in_playlist_table")
data class TrackInPlaylistEntity(
    @PrimaryKey
    val trackID: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    val isFavorite: Boolean, //не используем
    val addedTime: Long = System.currentTimeMillis()
)


