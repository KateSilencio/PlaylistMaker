package com.example.playlistmaker.medialib.domain.api

import com.example.playlistmaker.player.domain.models.TracksData
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksInteractor {

    suspend fun addTrackToFavorites(track: TracksData)
    suspend fun removeTrackFromFavorites(track: TracksData)
    fun getAllFavoriteTracks(): Flow<List<TracksData>>
}