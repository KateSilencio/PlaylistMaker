package com.example.playlistmaker.medialib.domain.impl

import android.net.Uri
import android.util.Log
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

    //*******************************************************************
    override fun getPlaylistById(id: Long): Flow<Playlist?> {
        return playlistRepository.getPlaylistById(id)
            .map { entity -> entity?.let { PlaylistMapper.map(it) } }
    }

    //суммируем длительность всех треков в плейлисте
    override suspend fun getPlaylistDuration(trackIds: List<Long>): Long {
        val tracks = playlistRepository.getTracksForPlaylist(trackIds)
        return tracks.sumOf { it.trackTimeMillis.toLong() }
    }

    override suspend fun getPlaylistEntityById(id: Long): PlaylistEntity? {
        return playlistRepository.getPlaylistEntityById(id)
    }

    //для показа треков в плейлисте
    override suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Long) {
        withContext(Dispatchers.IO) {
            playlistRepository.removeTrackFromPlaylist(playlistId, trackId)

            //если трек не исп в др плейлистах -> удаляем из таблицы
            if (!playlistRepository.isTrackInAnyPlaylist(trackId)) {
                playlistRepository.removeTrackFromTable(trackId)
            }
        }
    }

    // получаем список треков по их id из БД
    override suspend fun getTracksForPlaylist(trackIds: List<Long>): List<Track> {
        return playlistRepository.getTracksForPlaylist(trackIds)
    }

    override suspend fun deletePlaylist(playlistId: Long) {
        withContext(Dispatchers.IO) {
            playlistRepository.deletePlaylist(playlistId)

            val playlist = playlistRepository.getPlaylistEntityById(playlistId)
            // Удаляем файл обложки, если он существует
            playlist?.coverPath?.let { path ->
                try {
                    fileInteractor.deleteImage(path)
                } catch (e: Exception) {
                    Log.e("Error", "Error deleting cover image", e)
                }
            }
        }
    }

    //редактировать плейлист
    override suspend fun updatePlaylist(
        playlistId: Long,
        title: String,
        description: String?,
        coverUri: Uri?
    ) {
        withContext(Dispatchers.IO) {
            val currentPlaylist = playlistRepository.getPlaylistEntityById(playlistId) ?: return@withContext

            // Если есть старая обложка и
            // или новая обложка сущ-т или старая отличается от новой
            if (currentPlaylist.coverPath != null &&
                (coverUri != null || currentPlaylist.coverPath != coverUri?.toString())) {
                // Удаляем старую обложку
                fileInteractor.deleteImage(currentPlaylist.coverPath)
            }
            //сохр новую обложку если есть
            val coverPath = coverUri?.let {
                fileInteractor.saveImage(it)
            }
            //создаем новую версию плейлиста на основе текущего
            val updatedPlaylist = currentPlaylist.copy(
                title = title,
                description = description,
                coverPath = coverPath
            )
            //сохраняем изменения
            playlistRepository.updatePlaylist(updatedPlaylist)
        }
    }
}