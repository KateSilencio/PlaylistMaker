package com.example.playlistmaker.medialib.domain.api

import com.example.playlistmaker.medialib.data.model.PlaylistEntity
import com.example.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun savePlaylist(playlist: PlaylistEntity)

    fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    suspend fun addTrackToPlaylist(track: Track, playlist: PlaylistEntity): Boolean

    suspend fun isTrackInPlaylist(trackId: Int, playlistId: Long): Boolean

}