package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.player.domain.models.TracksParceling

sealed class SearchResultNetwork {
        data class Success(val tracks: List<TracksParceling>) : SearchResultNetwork()
        object Empty : SearchResultNetwork()
        object Error : SearchResultNetwork()

}