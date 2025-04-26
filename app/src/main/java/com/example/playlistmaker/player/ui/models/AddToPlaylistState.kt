package com.example.playlistmaker.player.ui.models

sealed class AddToPlaylistState {
    data class Success(val playlistName: String) : AddToPlaylistState()
    data class AlreadyAdded(val playlistName: String) : AddToPlaylistState()

}
