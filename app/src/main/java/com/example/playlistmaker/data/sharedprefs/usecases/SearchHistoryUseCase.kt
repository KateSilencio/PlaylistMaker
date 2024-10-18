package com.example.playlistmaker.data.sharedprefs.usecases

import com.example.playlistmaker.domain.models.TracksParceling
import java.util.LinkedList

interface SearchHistoryUseCase {

    fun saveTrack(track: TracksParceling): LinkedList<TracksParceling>
    fun getTracks(): LinkedList<TracksParceling>
    fun clearHistory()
}