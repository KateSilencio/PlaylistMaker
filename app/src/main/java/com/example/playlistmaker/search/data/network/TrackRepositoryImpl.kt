package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.dto.ITunesResponse
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.models.SearchResultNetwork
import com.example.playlistmaker.search.domain.models.TracksSearchRequest

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override suspend fun searchTracks(request: TracksSearchRequest): SearchResultNetwork {
        val response = networkClient.doRequest(TracksSearchRequest(request.entity, request.text))
        return when (response.resultCode) {
            200 -> SearchResultNetwork.Success((response as ITunesResponse).tracks)
            null -> SearchResultNetwork.Error
            else -> SearchResultNetwork.Empty
        }
    }
}