package com.example.playlistmaker.medialib.data.impl

import com.example.playlistmaker.medialib.data.db.AppDatabase
import com.example.playlistmaker.medialib.data.model.PlaylistEntity
import com.example.playlistmaker.medialib.data.model.toPlaylistTrackEntity
import com.example.playlistmaker.medialib.data.model.toTrack
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
            currentPlaylist.copy(
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

    //**********************************************************************
    override fun getPlaylistById(id: Long): Flow<PlaylistEntity> {
        return appDatabase.playlistDao().getPlaylistByIdFlow(id)
    }

    override suspend fun getTracksForPlaylist(trackIds: List<Long>): List<Track> {
        //получаем треки по id
        val trackEntities = appDatabase.trackInPlaylistDao().getTracksByIds(trackIds)
        //entity в домен модель track
        return trackEntities.map { it.toTrack() }
    }

    override suspend fun getPlaylistEntityById(id: Long): PlaylistEntity? {
        return appDatabase.playlistDao().getPlaylistById(id)
    }

    // для показа треков в плейлисте
    //удаление трека из плейлиста с обновл ids
    override suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Long) {
        // текущий плейлист
        val playlist = appDatabase.playlistDao().getPlaylistById(playlistId) ?: return

        // обновляем список треков, удаляем трек
        val updatedTrackIds = playlist.getTrackIds().toMutableList().apply {
            remove(trackId)
        }

        // обновляем плейлист
        val updatedPlaylist = playlist.copy(
            tracksJson = Gson().toJson(updatedTrackIds),
            trackCount = updatedTrackIds.size
        )
        appDatabase.playlistDao().updatePlaylist(updatedPlaylist)
    }

    //есть ли трек в любом плейлисте
    override suspend fun isTrackInAnyPlaylist(trackId: Long): Boolean {
        // получаем все плейлисты и проверяем наличие трека
        val playlists = appDatabase.playlistDao().getAllPlaylistsSync()
        return playlists.any { it.getTrackIds().contains(trackId) }
    }

    //удаление трека из табл треков (трек нигде не использ)
    override suspend fun removeTrackFromTable(trackId: Long) {
        appDatabase.trackInPlaylistDao().deleteTrack(trackId.toInt())
    }
    //удаление плейлиста
    override suspend fun deletePlaylist(playlistId: Long) {
        // получаем плейлист
        val playlist = appDatabase.playlistDao().getPlaylistById(playlistId) ?: return

        // получаем ID треков
        val trackIds = playlist.getTrackIds()

        // удаляем плейлист
        appDatabase.playlistDao().deletePlaylist(playlistId)

        // проверяем каждый трек исп ли он в др плейлистах
        // если не исп - удаляем
        trackIds.forEach { trackId ->
            if (!appDatabase.playlistDao().isTrackInAnyPlaylist(trackId)) {
                appDatabase.trackInPlaylistDao().deleteTrack(trackId.toInt())
            }
        }
    }
}