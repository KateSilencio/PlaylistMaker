package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.TracksSearchRequest
import com.example.playlistmaker.player.domain.models.TracksParceling

interface TracksInteractor {
    fun searchTracks(request: TracksSearchRequest, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundTracks: List<TracksParceling>)
        fun onError(error: ErrorType)
    }

    enum class ErrorType{
        SUCCESS,
        FAILURE,
        EMPTY_RESPONSE
    }
}