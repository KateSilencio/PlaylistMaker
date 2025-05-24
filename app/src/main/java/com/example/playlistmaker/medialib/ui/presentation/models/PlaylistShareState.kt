package com.example.playlistmaker.medialib.ui.presentation.models

sealed class PlaylistShareState {
    object Empty : PlaylistShareState()
    data class WithTracks(val shareText: String) : PlaylistShareState()
}