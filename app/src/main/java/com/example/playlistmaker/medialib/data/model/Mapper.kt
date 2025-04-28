package com.example.playlistmaker.medialib.data.model

import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.player.domain.models.TracksData

//конвертация для TrackInPlaylistEntity
fun Track.toPlaylistTrackEntity() = TrackInPlaylistEntity(
    trackID = this.trackId,
    trackName = this.trackName,
    artistName = this.artistName,
    trackTimeMillis = this.trackTimeMillis,
    artworkUrl100 = this.artworkUrl,
    collectionName = this.collectionName,
    releaseDate = this.releaseDate,
    primaryGenreName = this.primaryGenreName,
    country = this.country,
    previewUrl = this.previewUrl,
    isFavorite = this.isFavorite
)

// конвертация для TrackEntity
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

fun TrackEntity.toTrackDomain(): TracksData {
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
