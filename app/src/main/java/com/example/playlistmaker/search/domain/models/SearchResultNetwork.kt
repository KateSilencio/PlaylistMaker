package com.example.playlistmaker.search.domain.models

import com.example.playlistmaker.player.domain.models.TracksData

sealed class SearchResultNetwork {
        data class Success(val tracks: List<TracksData>) : SearchResultNetwork()
        object Empty : SearchResultNetwork()
        object Error : SearchResultNetwork()

}