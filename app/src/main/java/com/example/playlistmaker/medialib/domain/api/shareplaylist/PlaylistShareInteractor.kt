package com.example.playlistmaker.medialib.domain.api.shareplaylist

import com.example.playlistmaker.medialib.domain.model.Playlist
import com.example.playlistmaker.player.domain.models.TracksData

interface PlaylistShareInteractor {

    fun getShareText(playlist: Playlist, tracks: List<TracksData>): String

    fun onSharePlaylist(text: String)
}