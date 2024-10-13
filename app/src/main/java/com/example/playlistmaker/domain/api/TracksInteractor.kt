package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.TracksParceling
import com.example.playlistmaker.domain.models.TracksSearchRequest

interface TracksInteractor {
    fun searchTracks(request: TracksSearchRequest, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundTracks: List<TracksParceling>)
        fun onError(error: ErrorType)
    }

    enum class ErrorType{
        FAILURE,
        EMPTY_RESPONSE
    }
}