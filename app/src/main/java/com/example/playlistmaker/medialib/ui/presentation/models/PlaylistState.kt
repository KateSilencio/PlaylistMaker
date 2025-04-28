package com.example.playlistmaker.medialib.ui.presentation.models

import com.example.playlistmaker.medialib.domain.model.Playlist

sealed class PlaylistState {

    data class LoadedPlaylist(val playlists: List<Playlist>) : PlaylistState()
    object Empty : PlaylistState()
    object Error : PlaylistState()
}