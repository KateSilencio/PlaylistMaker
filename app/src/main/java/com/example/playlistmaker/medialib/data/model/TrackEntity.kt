package com.example.playlistmaker.medialib.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.playlistmaker.player.domain.models.TracksData

@Entity(tableName = "tracks_table")
data class TrackEntity(
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
    val isFavorite: Boolean,
    val addedTime: Long
)

fun TracksData.toEntity(): TrackEntity{
    return TrackEntity(
        trackID = trackID,
        trackName = trackName,
        artistName = artistName,
        trackTimeMillis = trackTimeMillis,
        artworkUrl100 = artworkUrl100,
        collectionName = collectionName,
        releaseDate = releaseDate,
        primaryGenreName = primaryGenreName,
        country = country,
        previewUrl = previewUrl,
        isFavorite = isFavorite,
        addedTime = System.currentTimeMillis()
    )
}

fun TrackEntity.toTrackDomain(): TracksData{
    return TracksData(
        trackID = trackID,
        trackName = trackName,
        artistName = artistName,
        trackTimeMillis = trackTimeMillis,
        artworkUrl100 = artworkUrl100,
        collectionName = collectionName,
        releaseDate = releaseDate,
        primaryGenreName = primaryGenreName,
        country = country,
        previewUrl = previewUrl,
        true
    )
}
