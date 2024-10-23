package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.player.domain.models.TracksParceling
import com.example.playlistmaker.search.domain.models.TracksSearchRequest
interface TracksRepository {
    fun searchTracks(request: TracksSearchRequest): List<TracksParceling>
}