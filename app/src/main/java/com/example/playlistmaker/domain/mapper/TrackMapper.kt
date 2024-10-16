package com.example.playlistmaker.domain.mapper

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.TracksParceling

object TrackMapper {
    fun toDomain(tracksParceling: TracksParceling): Track {
        return Track(
            trackName = tracksParceling.trackName,
            artistName = tracksParceling.artistName,
            trackTimeMillis = tracksParceling.trackTimeMillis,
            artworkUrl = tracksParceling.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"),
            collectionName = tracksParceling.collectionName,
            releaseDate = tracksParceling.releaseDate,
            primaryGenreName = tracksParceling.primaryGenreName,
            country = tracksParceling.country,
            previewUrl = tracksParceling.previewUrl
        )
    }
}
