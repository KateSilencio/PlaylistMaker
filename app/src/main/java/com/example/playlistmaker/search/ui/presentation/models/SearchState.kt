package com.example.playlistmaker.search.ui.presentation.models

import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.player.domain.models.TracksParceling

data class SearchState(
    val tracks: List<TracksParceling> = emptyList(),
    val historyTracks: List<TracksParceling> = emptyList(),
    val isProgressBarVisible: Boolean = false,
    val errorType: TracksInteractor.ErrorType? = null,
    var isClearScreenFlag: Boolean = false
)

/*enum class ResponseTypeSearch{
    SUCCESS,
    EMPTY_RESPONSE,
    FAILURE
}*/

