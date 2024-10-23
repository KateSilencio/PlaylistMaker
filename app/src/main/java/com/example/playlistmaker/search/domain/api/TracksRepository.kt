package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.data.network.SearchResultNetwork
import com.example.playlistmaker.search.domain.models.TracksSearchRequest

interface TracksRepository {
    //fun searchTracks(request: TracksSearchRequest): List<TracksParceling>
    fun searchTracks(request: TracksSearchRequest): SearchResultNetwork
}