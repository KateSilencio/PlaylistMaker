package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.player.domain.models.TracksData
import com.example.playlistmaker.search.domain.models.TracksSearchRequest

interface TracksInteractor {
    fun searchTracks(request: TracksSearchRequest, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundTracks: List<TracksData>)
        fun onError(error: ErrorType)
    }

    enum class ErrorType{
        FAILURE,
        EMPTY_RESPONSE
    }
}