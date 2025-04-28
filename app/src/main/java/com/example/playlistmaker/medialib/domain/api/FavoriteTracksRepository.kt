package com.example.playlistmaker.medialib.domain.api

import com.example.playlistmaker.player.domain.models.TracksData
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksRepository {

    suspend fun addTrackToFavorites(tracksData: TracksData)

    suspend fun removeTrackFromFavorites(tracksData: TracksData)

    fun getAllFavoriteTracks(): Flow<List<TracksData>>
}