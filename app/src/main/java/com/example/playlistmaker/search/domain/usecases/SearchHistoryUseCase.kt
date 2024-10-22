package com.example.playlistmaker.search.domain.usecases

import com.example.playlistmaker.player.domain.models.TracksParceling
import java.util.LinkedList

interface SearchHistoryUseCase {

    fun saveTrack(track: TracksParceling): LinkedList<TracksParceling>
    fun getTracks(): LinkedList<TracksParceling>
    fun clearHistory()
}