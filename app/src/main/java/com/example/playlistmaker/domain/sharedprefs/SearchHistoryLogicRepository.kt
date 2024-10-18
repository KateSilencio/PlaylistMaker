package com.example.playlistmaker.domain.sharedprefs

import com.example.playlistmaker.domain.models.TracksParceling
import java.util.LinkedList

interface SearchHistoryLogicRepository {

    fun saveTrack(track: TracksParceling): LinkedList<TracksParceling>
    fun getTracks(): LinkedList<TracksParceling>
    fun clearHistory()

}