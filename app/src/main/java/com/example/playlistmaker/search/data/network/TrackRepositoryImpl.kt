package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.dto.ITunesResponse
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.models.SearchResultNetwork
import com.example.playlistmaker.search.domain.models.TracksSearchRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override suspend fun searchTracks(request: TracksSearchRequest): Flow<SearchResultNetwork> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(request.entity, request.text))
        when (response.resultCode) {
            200 -> emit(SearchResultNetwork.Success((response as ITunesResponse).tracks))
            null -> emit(SearchResultNetwork.Error)
            else -> emit(SearchResultNetwork.Empty)
        }
    }
}