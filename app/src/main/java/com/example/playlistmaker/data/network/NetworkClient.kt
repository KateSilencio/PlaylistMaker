package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.domain.models.TracksSearchRequest

interface NetworkClient {
    fun doRequest(request: TracksSearchRequest): Response//Call<ITunesResponse>
}