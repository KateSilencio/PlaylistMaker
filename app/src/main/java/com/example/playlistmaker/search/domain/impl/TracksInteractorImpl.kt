package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.models.SearchResultNetwork
import com.example.playlistmaker.search.domain.models.TracksSearchRequest
import kotlinx.coroutines.flow.Flow

class TracksInteractorImpl(
    private val repository: TracksRepository
) : TracksInteractor {

    override suspend fun searchTracks(
        request: TracksSearchRequest
    ): Flow<SearchResultNetwork> {
        return repository.searchTracks(request)
    }
}