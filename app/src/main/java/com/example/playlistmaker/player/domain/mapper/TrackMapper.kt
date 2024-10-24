package com.example.playlistmaker.player.domain.mapper

import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.player.domain.models.TracksData

object TrackMapper {
    fun convertToTrack(tracksData: TracksData): Track {
        return Track(
            trackName = tracksData.trackName,
            artistName = tracksData.artistName,
            trackTimeMillis = tracksData.trackTimeMillis,
            artworkUrl = tracksData.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"),
            collectionName = tracksData.collectionName,
            releaseDate = tracksData.releaseDate,
            primaryGenreName = tracksData.primaryGenreName,
            country = tracksData.country,
            previewUrl = tracksData.previewUrl
        )
    }
}
