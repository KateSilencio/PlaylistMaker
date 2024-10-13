package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.ITunesResponse
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.TracksParceling
import com.example.playlistmaker.domain.models.TracksSearchRequest

class TrackRepositoryImpl(private val networkClient: NetworkClient): TracksRepository {

    override fun searchTracks(request: TracksSearchRequest): List<TracksParceling> {
        val response = networkClient.doRequest(TracksSearchRequest(request.entity,request.text))
        return if (response.resultCode == 200) {
            (response as ITunesResponse).tracks
        } else {
            emptyList()
        }
    }
}