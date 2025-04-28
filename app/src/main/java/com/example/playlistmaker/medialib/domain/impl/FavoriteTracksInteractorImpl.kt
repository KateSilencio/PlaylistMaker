package com.example.playlistmaker.medialib.domain.impl

import com.example.playlistmaker.medialib.domain.api.FavoriteTracksInteractor
import com.example.playlistmaker.medialib.domain.api.FavoriteTracksRepository
import com.example.playlistmaker.player.domain.models.TracksData
import kotlinx.coroutines.flow.Flow

class FavoriteTracksInteractorImpl(
    private val favoriteTracksRepository: FavoriteTracksRepository
): FavoriteTracksInteractor {
    override suspend fun addTrackToFavorites(track: TracksData) {
        favoriteTracksRepository.addTrackToFavorites(track)
    }

    override suspend fun removeTrackFromFavorites(track: TracksData) {
        favoriteTracksRepository.removeTrackFromFavorites(track)
    }

    override fun getAllFavoriteTracks(): Flow<List<TracksData>> {
        return favoriteTracksRepository.getAllFavoriteTracks()
    }
}