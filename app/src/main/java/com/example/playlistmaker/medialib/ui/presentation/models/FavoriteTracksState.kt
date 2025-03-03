package com.example.playlistmaker.medialib.ui.presentation.models

import com.example.playlistmaker.player.domain.models.TracksData

sealed interface FavoriteTracksState {
    object Loading: FavoriteTracksState
    object NoData: FavoriteTracksState
    data class LoadedTracks(val  tracks: List<TracksData>): FavoriteTracksState

}