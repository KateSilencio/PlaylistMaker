package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.models.SearchResultNetwork
import com.example.playlistmaker.search.domain.models.TracksSearchRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksInteractorImpl(
    private val repository: TracksRepository
) : TracksInteractor {

    override fun searchTracks(
        request: TracksSearchRequest
    ): Flow<SearchResultNetwork> = flow {

        when (val result = repository.searchTracks(request)) {
            is SearchResultNetwork.Success -> emit(SearchResultNetwork.Success(result.tracks))
            is SearchResultNetwork.Empty -> emit(SearchResultNetwork.Empty)
            is SearchResultNetwork.Error -> emit(SearchResultNetwork.Error)
        }
    }
}