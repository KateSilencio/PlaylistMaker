package com.example.playlistmaker.medialib.domain.impl

import android.net.Uri
import com.example.playlistmaker.medialib.data.model.PlaylistEntity
import com.example.playlistmaker.medialib.domain.api.PlaylistInteractor
import com.example.playlistmaker.medialib.domain.api.PlaylistRepository
import com.example.playlistmaker.medialib.domain.filestore.FileInteractor
import com.example.playlistmaker.medialib.domain.model.Playlist
import com.example.playlistmaker.medialib.domain.model.PlaylistMapper
import com.example.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository,
    private val fileInteractor: FileInteractor
) : PlaylistInteractor {
    override suspend fun createPlaylist(
        title: String,
        description: String?,
        coverUri: Uri?
    ) {
        withContext(Dispatchers.IO){
            //путь обложки, если есть
            val coverPath = coverUri?.let { fileInteractor.saveImage(it) }
            val playlist = PlaylistEntity.create(
                title = title,
                description = description,
                coverPath = coverPath,
                trackIds = emptyList() //список пустой при создании
            )

            playlistRepository.savePlaylist(playlist)
        }
    }

    override fun getAllPlaylists(): Flow<List<PlaylistEntity>> {
        return playlistRepository.getAllPlaylists()
    }
    // для показа созданных плейлистов:
    override fun getPlaylists(): Flow<List<Playlist>> {
        return getAllPlaylists().map { list ->
            list.map { PlaylistMapper.map(it) }
        }
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: PlaylistEntity): Boolean {
        return playlistRepository.addTrackToPlaylist(track, playlist)
    }

    override suspend fun isTrackInPlaylist(trackId: Int, playlistId: Long): Boolean {
        return playlistRepository.isTrackInPlaylist(trackId, playlistId)
    }
}