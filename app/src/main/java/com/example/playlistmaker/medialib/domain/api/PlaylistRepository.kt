package com.example.playlistmaker.medialib.domain.api

import com.example.playlistmaker.medialib.data.model.PlaylistEntity
import com.example.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun savePlaylist(playlist: PlaylistEntity)

    fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    suspend fun addTrackToPlaylist(track: Track, playlist: PlaylistEntity): Boolean

    suspend fun isTrackInPlaylist(trackId: Int, playlistId: Long): Boolean
    //***********************************************
    fun getPlaylistById(id: Long): Flow<PlaylistEntity?>

    suspend fun getTracksForPlaylist(trackIds: List<Long>): List<Track>

    suspend fun getPlaylistEntityById(id: Long): PlaylistEntity?

    suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Long)

    suspend fun isTrackInAnyPlaylist(trackId: Long): Boolean

    suspend fun removeTrackFromTable(trackId: Long)

    suspend fun deletePlaylist(playlistId: Long)

    suspend fun updatePlaylist(playlist: PlaylistEntity)
}