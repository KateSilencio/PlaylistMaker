package com.example.playlistmaker.medialib.data.impl

import com.example.playlistmaker.medialib.data.db.AppDatabase
import com.example.playlistmaker.medialib.data.model.PlaylistEntity
import com.example.playlistmaker.medialib.data.model.toPlaylistTrackEntity
import com.example.playlistmaker.medialib.domain.api.PlaylistRepository
import com.example.playlistmaker.player.domain.models.Track
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow

class PlaylistRepositoryImpl(private val appDatabase: AppDatabase): PlaylistRepository {
    override suspend fun savePlaylist(playlist: PlaylistEntity) {
        appDatabase.playlistDao().insertPlaylist(playlist)  //перевод данных в сущность???
    }

    override fun getAllPlaylists(): Flow<List<PlaylistEntity>> {
        return appDatabase.playlistDao().getAllPlaylists()
    }

    //новые функции для плейлистов в аудиоплеере
    override suspend fun addTrackToPlaylist(track: Track, playlist: PlaylistEntity): Boolean {
        // Track в PlaylistTrackEntity
        val playlistTrack = track.toPlaylistTrackEntity()
        // Сохр playlistTrack в таблицу плейлистов
        appDatabase.trackInPlaylistDao().insertTrack(playlistTrack)
        //получаем тек версию плейлиста
        val currentPlaylist = appDatabase.playlistDao().getPlaylistById(playlist.playlistId)
        // трек уже есть
        if (currentPlaylist != null) {
            if (currentPlaylist.getTrackIds().contains(track.trackId.toLong())) {
                return false
            }
        }
        //Обновляем список trackIds в плейлисте
        val updatedTrackIds = currentPlaylist?.getTrackIds()?.toMutableList()?.apply {
            add(track.trackId.toLong())
        }

        // Создаём обновлённую версию плейлиста
        val updatedPlaylist = updatedTrackIds?.let {
            currentPlaylist?.copy(
                tracksJson = Gson().toJson(updatedTrackIds),
                trackCount = it.size
            )
        }
        if (updatedPlaylist != null) {
            appDatabase.playlistDao().updatePlaylist(updatedPlaylist)
        }

        return true
    }

    override suspend fun isTrackInPlaylist(trackId: Int, playlistId: Long): Boolean {

        val playlist = appDatabase.playlistDao().getPlaylistById(playlistId)
        // есть ли трек в плейлисте
        return playlist?.getTrackIds()?.contains(trackId.toLong()) ?: false
    }
}