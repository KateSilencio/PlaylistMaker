package com.example.playlistmaker.medialib.domain.api.shareplaylist

interface PlaylistShareNavigationRepository {
    fun sharePlaylist(text: String)
}