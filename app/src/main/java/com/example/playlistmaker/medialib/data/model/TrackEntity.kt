package com.example.playlistmaker.medialib.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.playlistmaker.player.domain.models.TracksData

@Entity(tableName = "tracks_table")
data class TrackEntity(
    @PrimaryKey
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    val isFavorite: Boolean
)

fun TracksData.toEntity(): TrackEntity{
    return TrackEntity(
        trackId = trackID,
        trackName = trackName,
        artistName = artistName,
        trackTimeMillis = trackTimeMillis,
        artworkUrl100 = artworkUrl100,
        collectionName = collectionName,
        releaseDate = releaseDate,
        primaryGenreName = primaryGenreName,
        country = country,
        previewUrl = previewUrl,
        isFavorite = isFavorite
    )
}

fun TrackEntity.toTrackDomain(): TracksData{
    return TracksData(
        trackID = trackId,
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
