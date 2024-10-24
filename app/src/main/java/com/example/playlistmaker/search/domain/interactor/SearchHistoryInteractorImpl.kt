package com.example.playlistmaker.search.domain.interactor

import com.example.playlistmaker.player.domain.models.TracksData
import com.example.playlistmaker.search.domain.datastore.SearchHistoryLogicRepository
import java.util.LinkedList

class SearchHistoryInteractorImpl(private val historyRepository: SearchHistoryLogicRepository) :
    SearchHistoryInteractor {
    override fun saveTrack(track: TracksData): LinkedList<TracksData> {
        return historyRepository.saveTrack(track)
    }

    override fun getTracks(): LinkedList<TracksData> {
        return historyRepository.getTracks()
    }

    override fun clearHistory() {
        historyRepository.clearHistory()
    }
}