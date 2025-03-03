package com.example.playlistmaker.medialib.data.impl

import com.example.playlistmaker.medialib.data.db.AppDatabase
import com.example.playlistmaker.medialib.data.model.toEntity
import com.example.playlistmaker.medialib.data.model.toTrackDomain
import com.example.playlistmaker.medialib.domain.FavoriteTracksRepository
import com.example.playlistmaker.player.domain.models.TracksData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteTracksRepositoryImpl(private val appDatabase: AppDatabase):FavoriteTracksRepository {
    override suspend fun addTrackToFavorites(tracksData: TracksData) {
        appDatabase.tracksDAO().insertTrack(tracksData.toEntity())
    }

    override suspend fun removeTrackFromFavorites(tracksData: TracksData) {
        appDatabase.tracksDAO().deleteTrack(tracksData.toEntity())
    }

    override fun getAllFavoriteTracks(): Flow<List<TracksData>> {
        return appDatabase.tracksDAO().getAllTracks().map { trackEntity ->
            trackEntity.map { it.toTrackDomain() }
        }
    }
}