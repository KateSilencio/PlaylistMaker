package com.example.playlistmaker.player.domain.mapper

import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.player.domain.models.TracksData

object TrackMapper {
    fun convertToTrack(tracksData: TracksData): Track {
        return Track(
            trackId = tracksData.trackID,
            trackName = tracksData.trackName,
            artistName = tracksData.artistName,
            trackTimeMillis = tracksData.trackTimeMillis,
            artworkUrl = tracksData.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"),
            collectionName = tracksData.collectionName,
            releaseDate = tracksData.releaseDate,
            primaryGenreName = tracksData.primaryGenreName,
            country = tracksData.country,
            previewUrl = tracksData.previewUrl,
            isFavorite = tracksData.isFavorite
        )
    }

    fun convertToTracksData(track: Track): TracksData{
        return TracksData(
            trackID = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
            isFavorite = track.isFavorite
        )
    }
}
