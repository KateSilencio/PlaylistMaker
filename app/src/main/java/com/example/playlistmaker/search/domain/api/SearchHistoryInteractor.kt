package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.player.domain.models.TracksData
import java.util.LinkedList

interface SearchHistoryInteractor {

    suspend fun saveTrack(track: TracksData): LinkedList<TracksData>
    suspend fun getTracks(): LinkedList<TracksData>
    fun clearHistory()
}