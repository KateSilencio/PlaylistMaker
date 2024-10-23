package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.dto.ITunesResponse
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.models.TracksSearchRequest

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    //    override fun searchTracks(request: TracksSearchRequest): List<TracksParceling> {
//        val response = networkClient.doRequest(TracksSearchRequest(request.entity, request.text))
//        return if (response.resultCode == 200) {
//            (response as ITunesResponse).tracks
//        } else {
//            emptyList()
//        }
//    }
    override fun searchTracks(request: TracksSearchRequest): SearchResultNetwork {
        val response = networkClient.doRequest(TracksSearchRequest(request.entity, request.text))
        return when (response.resultCode) {
            200 -> SearchResultNetwork.Success((response as ITunesResponse).tracks)
            null -> SearchResultNetwork.Error
            else -> SearchResultNetwork.Empty
        }
    }
}