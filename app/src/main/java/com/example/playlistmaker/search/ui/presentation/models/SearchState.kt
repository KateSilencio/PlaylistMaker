package com.example.playlistmaker.search.ui.presentation.models

import com.example.playlistmaker.player.domain.models.TracksParceling

//data class SearchState(
//    val tracks: List<TracksParceling> = emptyList(),
//    val historyTracks: List<TracksParceling> = emptyList(),
//    val isProgressBarVisible: Boolean = false,
//    val errorType: TracksInteractor.ErrorType? = null,
//    var isClearScreenFlag: Boolean = false
//)
sealed interface SearchState {
    object Default: SearchState
    object Loading : SearchState
    object NoData : SearchState
    object NothingFound : SearchState
    data class ConnectionError(val error: Int) : SearchState
    data class TrackSearchResults(val results: List<TracksParceling>) : SearchState
    data class TrackSearchHistory(var history: MutableList<TracksParceling>) : SearchState
}

