package com.example.playlistmaker.domain.api

import com.example.playlistmaker.player.domain.models.TracksParceling
import com.example.playlistmaker.domain.models.TracksSearchRequest
interface TracksRepository {
    fun searchTracks(request: TracksSearchRequest): List<TracksParceling>
}