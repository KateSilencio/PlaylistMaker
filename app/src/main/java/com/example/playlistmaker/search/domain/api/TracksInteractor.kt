package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.SearchResultNetwork
import com.example.playlistmaker.search.domain.models.TracksSearchRequest
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    suspend fun searchTracks(request: TracksSearchRequest): Flow<SearchResultNetwork>

    //нужна ли эта функция в presentation???
    suspend fun getFavoriteTracksByID(): List<Int>
}