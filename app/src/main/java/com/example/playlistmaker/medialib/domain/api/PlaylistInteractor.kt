package com.example.playlistmaker.medialib.domain.api

import android.net.Uri
import com.example.playlistmaker.medialib.data.model.PlaylistEntity
import com.example.playlistmaker.medialib.domain.model.Playlist
import com.example.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun createPlaylist(
        title: String,
        description: String?,
        coverUri: Uri?
    )

    fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun addTrackToPlaylist(track: Track, playlist: PlaylistEntity): Boolean

    suspend fun isTrackInPlaylist(trackId: Int, playlistId: Long): Boolean

    //********************************************
    fun getPlaylistById(id: Long): Flow<Playlist?>

    suspend fun getPlaylistDuration(trackIds: List<Long>): Long

    suspend fun getPlaylistEntityById(id: Long): PlaylistEntity?

    //для показа треков в плейлисте
    suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Long)

    suspend fun getTracksForPlaylist(trackIds: List<Long>): List<Track>

    suspend fun deletePlaylist(playlistId: Long)
}