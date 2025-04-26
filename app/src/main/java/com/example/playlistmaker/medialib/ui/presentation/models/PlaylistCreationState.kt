package com.example.playlistmaker.medialib.ui.presentation.models

interface PlaylistCreationState {
    object Saving : PlaylistCreationState
    data class Success(val playlistName: String) : PlaylistCreationState
    object Canceled : PlaylistCreationState
}