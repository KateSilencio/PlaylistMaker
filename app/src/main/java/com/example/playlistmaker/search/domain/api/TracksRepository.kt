package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.SearchResultNetwork
import com.example.playlistmaker.search.domain.models.TracksSearchRequest
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    suspend fun searchTracks(request: TracksSearchRequest): Flow<SearchResultNetwork>
    suspend fun getFavoriteTracksByID(): List<Int>
}