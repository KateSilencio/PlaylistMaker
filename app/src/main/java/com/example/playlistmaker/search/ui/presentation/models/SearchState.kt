package com.example.playlistmaker.search.ui.presentation.models

import com.example.playlistmaker.player.domain.models.TracksData

sealed interface SearchState {
    object Default: SearchState
    object Loading : SearchState
    object NoData : SearchState
    object NothingFound : SearchState
    data class ConnectionError(val error: Int) : SearchState
    data class TrackSearchResults(val results: List<TracksData>) : SearchState
    data class TrackSearchHistory(var history: MutableList<TracksData>) : SearchState
}

