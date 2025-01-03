package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.SearchResultNetwork
import com.example.playlistmaker.search.domain.models.TracksSearchRequest

interface TracksRepository {
    suspend fun searchTracks(request: TracksSearchRequest): SearchResultNetwork
}