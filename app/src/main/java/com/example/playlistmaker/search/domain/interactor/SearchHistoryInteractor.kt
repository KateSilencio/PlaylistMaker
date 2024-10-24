package com.example.playlistmaker.search.domain.interactor

import com.example.playlistmaker.player.domain.models.TracksData
import java.util.LinkedList

interface SearchHistoryInteractor {

    fun saveTrack(track: TracksData): LinkedList<TracksData>
    fun getTracks(): LinkedList<TracksData>
    fun clearHistory()
}